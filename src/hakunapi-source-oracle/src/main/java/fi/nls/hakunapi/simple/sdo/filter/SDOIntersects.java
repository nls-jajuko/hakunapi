package fi.nls.hakunapi.simple.sdo.filter;

public class SDOIntersects extends SDOGeometryFunction {

    @Override
    public String getMask() {
        return "ANYINTERACT";
    }

}
