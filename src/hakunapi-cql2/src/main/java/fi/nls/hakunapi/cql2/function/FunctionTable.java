package fi.nls.hakunapi.cql2.function;

import java.util.List;

public interface FunctionTable<TContext> {

    public String getPackageName();

    // [<packageName>_]<functionName>
    public Function<TContext> getFunction(String functionName);

    public void getFunctions(List<Function<TContext>> list);

    public boolean isHidden();

}
