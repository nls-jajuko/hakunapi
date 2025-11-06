package fi.nls.hakunapi.cql2.function;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import fi.nls.hakunapi.cql2.model.FilterContext;

public class FunctionTableProvider {

    private static final ServiceLoader<FunctionTableFactory> LOADER = ServiceLoader.load(FunctionTableFactory.class);

    public static List<FunctionTable<FilterContext>> getFunctionTables() {
        LOADER.reload();

        final List<FunctionTable<FilterContext>> functionTables = new ArrayList<>();

        for (FunctionTableFactory factory : LOADER) {
            functionTables.addAll(factory.createFunctionTables());
        }
        return functionTables;
    }

}
