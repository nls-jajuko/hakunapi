package fi.nls.hakunapi.cql2.function.geometry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;

import fi.nls.hakunapi.core.SRIDCode;
import fi.nls.hakunapi.core.projection.ProjectionHelper;
import fi.nls.hakunapi.core.projection.ProjectionTransformer;
import fi.nls.hakunapi.core.schemas.FunctionArgumentInfo.FunctionArgumentType;
import fi.nls.hakunapi.core.schemas.FunctionReturnsInfo.FunctionReturnsType;
import fi.nls.hakunapi.cql2.function.Function;
import fi.nls.hakunapi.cql2.model.FilterContext;

public class ST_Buffer extends Function<FilterContext> {

    /*
     * Proof-of-Concept ST_Buffer with some form of SRID specifics
     * 
     * reference: http://postgis.net/docs/manual-3.2/ST_Buffer.html
     * ST_Buffer(geom,radius_of_buffer,num_seg_quarter_circle,
     * buffer_style_parameters )
     * geom,radius_of_buffer,num_seg_quarter_circle,buffer_style_parameters
     */

    public ST_Buffer() {
        super("ST_Buffer", null, null);
        argument("geom", FunctionArgumentType.geometry);
        argument("radius_of_buffer", FunctionArgumentType.number);
        argument("buffer_style_parameters", FunctionArgumentType.string);

        returns(FunctionReturnsType.geometry);
    }

    @Override
    public Object invoke(List<Object> args, FilterContext fContext) {
        Geometry givenGeom = getGeometryArg(args, "geom");
        double radius_of_buffer = getNumberArg(args, "radius_of_buffer").doubleValue();
        String buffer_style_parameters = getStringArg(args, "buffer_style_parameters");
        String[] parts = buffer_style_parameters.split(" ");
        final Map<String, String> kv = Stream.of(parts).filter(v -> !v.isEmpty()).map(elem -> elem.split("="))
                .filter(v -> v.length != 0).collect(Collectors.toMap(e -> e[0], e -> e[1]));

        int endCapStyle = mapBufferStyleParameter(kv);
        int numSeq = mapNumSegQuarterCircleParameter(kv);

        if (fContext == null || !fContext.filterSrid().isDegrees()) {
            return givenGeom.buffer(radius_of_buffer, numSeq, endCapStyle);
        }

        int filterSrid = fContext.filterSrid().getSrid();
        SRIDCode storageSrid = fContext.storageSrid().orElseThrow();
        int viaSrid = !storageSrid.isDegrees() ? storageSrid.getSrid() : 3857;
        Geometry geom = givenGeom.copy();
        try {
            ProjectionTransformer fromFiltertoVia = fContext.projectionTransformer().orElseThrow()
                    .getTransformer(filterSrid, viaSrid);
            ProjectionTransformer fromViaToFilter = fContext.projectionTransformer().orElseThrow()
                    .getTransformer(viaSrid, filterSrid);

            Geometry geomJTS = ProjectionHelper.reproject(geom, fromFiltertoVia);
            geomJTS = BufferOp.bufferOp(geomJTS, radius_of_buffer, numSeq, endCapStyle);
            geomJTS = ProjectionHelper.reproject(geomJTS, fromViaToFilter);

            return geomJTS;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private int mapBufferStyleParameter(final Map<String, String> kv) {
        final String join = kv.getOrDefault("join", "round");// 'join=round|mitre|bevel'

        switch (join) {

        case "miter":
        case "mitre":
            return BufferParameters.JOIN_MITRE;
        case "bevel":
            return BufferParameters.JOIN_BEVEL;
        case "round":
        default:
            return BufferParameters.JOIN_ROUND;
        }
    }

    private int mapNumSegQuarterCircleParameter(final Map<String, String> kv) {
        return Integer.parseInt(kv.getOrDefault("quad_segs", "8")); // 'quad_segs=#' default 8
    }

}
