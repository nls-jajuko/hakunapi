package fi.nls.hakunapi.cql2.function;

import java.util.List;

import fi.nls.hakunapi.cql2.model.FilterContext;

public interface FunctionTableFactory {

    public List<FunctionTable<FilterContext>> createFunctionTables();
}
