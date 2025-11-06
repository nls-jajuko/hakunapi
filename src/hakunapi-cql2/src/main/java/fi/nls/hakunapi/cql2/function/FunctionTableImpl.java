package fi.nls.hakunapi.cql2.function;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fi.nls.hakunapi.cql2.model.FilterContext;

public class FunctionTableImpl implements FunctionTable<FilterContext> {

    final String packageName;
    final Map<String, Function<FilterContext>> functions = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    boolean hidden = false;

    @Override
    public void getFunctions(List<Function<FilterContext>> list) {
        list.addAll(functions.values());
    }

    @Override
    // [<packageName>_]<functionName>
    public Function<FilterContext> getFunction(String functionName) {
        return functions.get(functionName);
    }

    public void putFunction(Function<FilterContext> func) {
        functions.put(func.getName(), func);
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    public FunctionTableImpl(String packageName, boolean hidden) {
        super();
        this.packageName = packageName;
        this.hidden = hidden;
    }

    public FunctionTableImpl(String packageName) {
        super();
        this.packageName = packageName;
        this.hidden = false;
    }

    public static FunctionTableImpl of(String packageName, List<Function<FilterContext>> functions) {

        final FunctionTableImpl impl = new FunctionTableImpl(packageName);
        functions.forEach(f -> impl.putFunction(f));
        return impl;

    }

}
