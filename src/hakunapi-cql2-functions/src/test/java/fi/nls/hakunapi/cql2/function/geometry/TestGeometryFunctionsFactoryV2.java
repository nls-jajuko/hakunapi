package fi.nls.hakunapi.cql2.function.geometry;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import fi.nls.hakunapi.core.FeatureProducer;
import fi.nls.hakunapi.core.SRIDCode;
import fi.nls.hakunapi.core.SimpleFeatureType;
import fi.nls.hakunapi.core.geom.HakunaGeometryDimension;
import fi.nls.hakunapi.core.geom.HakunaGeometryType;
import fi.nls.hakunapi.core.projection.ProjectionTransformerFactory;
import fi.nls.hakunapi.core.property.simple.HakunaPropertyGeometry;
import fi.nls.hakunapi.cql2.function.Function;
import fi.nls.hakunapi.cql2.function.FunctionTable;
import fi.nls.hakunapi.cql2.model.FilterContext;
import fi.nls.hakunapi.cql2.model.SimpleFilterContext;
import fi.nls.hakunapi.proj.gt.GeoToolsProjectionTransformerFactory;

public class TestGeometryFunctionsFactoryV2 {

    static GeometryFactory geomFactory = new GeometryFactory();
    static GeometryFunctionsFactory factory = new GeometryFunctionsFactory();
    static List<FunctionTable<FilterContext>> functionTables = factory.createFunctionTables();

    @Test
    public void testGeometryFunctionsBufferDegreesWithContext() {

        Function<FilterContext> buffer = functionTables.get(0).getFunction("Buffer");
        assertNotNull(buffer);

        SRIDCode srid84 = new SRIDCode(84, false, true, HakunaGeometryDimension.XY);
        SRIDCode srid3067 = new SRIDCode(3067, false, false, HakunaGeometryDimension.XY);
        SRIDCode storageSrid = srid3067;

        SimpleFeatureType ft = new SimpleFeatureType() {

            @Override
            public FeatureProducer getFeatureProducer() {
                return null;
            }
        };

        ProjectionTransformerFactory ptf = new GeoToolsProjectionTransformerFactory();
        ft.setProjectionTransformerFactory(ptf);
        ft.setKnownSrids(List.of(srid84, srid3067));
        HakunaPropertyGeometry geomType = new HakunaPropertyGeometry("geometry", "NULL", "geom", false,
                HakunaGeometryType.POINT, new int[] { srid3067.getSrid(), srid84.getSrid() }, storageSrid.getSrid(), 2,
                (vp, i, w) -> {
                });
        ft.setGeom(geomType);

        double[] coordinates = new double[] { 24, 60 };
        Point geomFrom = geomFactory.createPoint(new Coordinate(coordinates[0], coordinates[1]));
        geomFrom.setSRID(srid84.getSrid());

        FilterContext fc = new SimpleFilterContext(ft, srid84);
        Object rv = buffer.invoke(List.of(geomFrom, 1), fc);

        assertNotNull(rv);
        assertTrue(rv instanceof Polygon);
    }

    @Test
    public void testGeometryFunctionsBuffer() {
        Function<?> buffer = functionTables.get(0).getFunction("Buffer");
        assertNotNull(buffer);

        LineString geomFrom = geomFactory
                .createLineString(new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 1) });

        Object rv = buffer.invoke(List.of(geomFrom, 1), null);

        assertNotNull(rv);
        assertTrue(rv instanceof Polygon);
    }

    @Test
    public void testGeometryFunctionsSTBuffer() {
        Function<?> st_buffer = functionTables.get(0).getFunction("ST_Buffer");
        assertNotNull(st_buffer);

        LineString geomFrom = geomFactory
                .createLineString(new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 1) });

        Object rv = st_buffer.invoke(List.of(geomFrom, 1, ""), null);

        assertNotNull(rv);
        assertTrue(rv instanceof Polygon);
    }

    @Test
    public void testGeometryFunctionsSTBufferDegreesWithContext() {
        Function<FilterContext> buffer = functionTables.get(0).getFunction("ST_Buffer");
        assertNotNull(buffer);

        SRIDCode srid84 = new SRIDCode(84, false, true, HakunaGeometryDimension.XY);
        SRIDCode srid3067 = new SRIDCode(3067, false, false, HakunaGeometryDimension.XY);
        SRIDCode storageSrid = srid3067;

        SimpleFeatureType ft = new SimpleFeatureType() {

            @Override
            public FeatureProducer getFeatureProducer() {
                return null;
            }
        };

        ProjectionTransformerFactory ptf = new GeoToolsProjectionTransformerFactory();
        ft.setProjectionTransformerFactory(ptf);
        ft.setKnownSrids(List.of(srid84, srid3067));
        HakunaPropertyGeometry geomType = new HakunaPropertyGeometry("geometry", "NULL", "geom", false,
                HakunaGeometryType.POINT, new int[] { srid3067.getSrid(), srid84.getSrid() }, storageSrid.getSrid(), 2,
                (vp, i, w) -> {
                });
        ft.setGeom(geomType);

        double[] coordinates = new double[] { 24, 60 };
        Point geomFrom = geomFactory.createPoint(new Coordinate(coordinates[0], coordinates[1]));
        geomFrom.setSRID(srid84.getSrid());

        FilterContext fc = new SimpleFilterContext(ft, srid84);
        Object rv = buffer.invoke(List.of(geomFrom, 1, "join=round quad_segs=8"), fc);

        assertNotNull(rv);
        assertTrue(rv instanceof Polygon);
    }

    @Test
    public void testGeometryFunctionsSTBufferMiter() {
        Function<?> st_buffer = functionTables.get(0).getFunction("ST_Buffer");
        assertNotNull(st_buffer);

        LineString geomFrom = geomFactory
                .createLineString(new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 1) });

        Object rv = st_buffer.invoke(List.of(geomFrom, 1, "join=miter"), null);

        assertNotNull(rv);
        assertTrue(rv instanceof Polygon);
    }

}
