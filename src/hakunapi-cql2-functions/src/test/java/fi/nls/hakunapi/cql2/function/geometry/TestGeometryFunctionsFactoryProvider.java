package fi.nls.hakunapi.cql2.function.geometry;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;

import fi.nls.hakunapi.cql2.function.Function;
import fi.nls.hakunapi.cql2.function.FunctionTable;
import fi.nls.hakunapi.cql2.function.FunctionTableProvider;
import fi.nls.hakunapi.cql2.model.FilterContext;

public class TestGeometryFunctionsFactoryProvider {

    static Map<String, FunctionTable<FilterContext>> FUNCTION_TABLES = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    static GeometryFactory geomFactory = new GeometryFactory();
    static GeometryFunctionsFactory factory;

    @BeforeClass
    public static void init() {
        FunctionTableProvider.getFunctionTables().forEach(ft -> {
            FUNCTION_TABLES.put(ft.getPackageName(), ft);
        });
    }

    Optional<Function<FilterContext>> getAnyFunction(String functionName) {
        return FUNCTION_TABLES.entrySet().stream().map(entry -> entry.getValue().getFunction(functionName))
                .filter(Objects::nonNull).findFirst();
    }

    @Test
    public void testGeometryFunctionsBuffer() {

        Function<?> buffer = getAnyFunction("Buffer").orElseThrow();

        LineString geomFrom = geomFactory
                .createLineString(new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 1) });

        Object rv = buffer.invoke(List.of(geomFrom, 1), null);

        assertNotNull(rv);
        assertTrue(rv instanceof Polygon);
    }

    @Test
    public void testGeometryFunctionsSTBuffer() {
        Function<?> st_buffer = getAnyFunction("ST_Buffer").orElseThrow();

        LineString geomFrom = geomFactory
                .createLineString(new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 1) });

        Object rv = st_buffer.invoke(List.of(geomFrom, 1, ""), null);

        assertNotNull(rv);
        assertTrue(rv instanceof Polygon);
    }

    @Test
    public void testGeometryFunctionsSTBufferMiter() {

        Function<?> st_buffer = getAnyFunction("ST_Buffer").orElseThrow();

        LineString geomFrom = geomFactory
                .createLineString(new Coordinate[] { new Coordinate(0, 0), new Coordinate(1, 1) });

        Object rv = st_buffer.invoke(List.of(geomFrom, 1, "join=miter"), null);

        assertNotNull(rv);
        assertTrue(rv instanceof Polygon);
    }

}
