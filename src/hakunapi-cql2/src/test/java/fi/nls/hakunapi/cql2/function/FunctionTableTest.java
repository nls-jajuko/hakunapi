package fi.nls.hakunapi.cql2.function;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import fi.nls.hakunapi.core.schemas.FunctionArgumentInfo.FunctionArgumentType;
import fi.nls.hakunapi.cql2.model.FilterContext;

public class FunctionTableTest {

    @Test
    public void testFunction() {

        FunctionTableImpl functions = FunctionTableImpl.of("poc", List.of(
                //
                Function.<FilterContext>of("echo", (func, args, ctx) -> {
                    return func.getStringArg(args, "param");
                }).argument("param", FunctionArgumentType.string),
                //
                Function.<FilterContext>of("numToString", (func, args, ctx) -> {
                    return func.getNumberArg(args, "arg1").toString();
                }).argument("arg1", FunctionArgumentType.number)));

        assertTrue("A".equals(functions.getFunction("echo").invoke(List.of("A"), null)));
        assertTrue("1337.0".equals(functions.getFunction("numToString").invoke(List.of(1337.0), null)));

    }
}
