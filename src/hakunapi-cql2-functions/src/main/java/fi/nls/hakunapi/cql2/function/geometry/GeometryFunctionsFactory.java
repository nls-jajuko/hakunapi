package fi.nls.hakunapi.cql2.function.geometry;

import java.util.List;

import fi.nls.hakunapi.cql2.function.FunctionTable;
import fi.nls.hakunapi.cql2.function.FunctionTableFactory;
import fi.nls.hakunapi.cql2.function.FunctionTableImpl;
import fi.nls.hakunapi.cql2.model.FilterContext;

public class GeometryFunctionsFactory implements FunctionTableFactory {

    @Override
    public List<FunctionTable<FilterContext>> createFunctionTables() {

        return List.of(FunctionTableImpl.of("geometry_editors", List.of(
                //
                new ST_Buffer(),
                //
                new Buffer())));
    }

}
