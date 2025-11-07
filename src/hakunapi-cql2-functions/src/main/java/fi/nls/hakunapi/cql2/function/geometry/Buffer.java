package fi.nls.hakunapi.cql2.function.geometry;

import java.util.List;

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

public class Buffer extends Function<FilterContext> {

    /* Proof-of-Concept Buffer with some form of SRID specifics */

    public Buffer() {
        super("Buffer", null, null);
        argument("geom", FunctionArgumentType.geometry);
        argument("radius_of_buffer", FunctionArgumentType.number);

        returns(FunctionReturnsType.geometry);
    }

    @Override
    public Object invoke(List<Object> args, FilterContext fContext) {
        Geometry givenGeom = getGeometryArg(args, "geom");
        double radius_of_buffer = getNumberArg(args, "radius_of_buffer").doubleValue();
        
        if (fContext == null || !fContext.filterSrid().isDegrees()) {
            return givenGeom.buffer(radius_of_buffer);
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
            geomJTS = BufferOp.bufferOp(geomJTS, radius_of_buffer, 8, BufferParameters.CAP_ROUND);
            geomJTS = ProjectionHelper.reproject(geomJTS, fromViaToFilter);

            return geomJTS;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}