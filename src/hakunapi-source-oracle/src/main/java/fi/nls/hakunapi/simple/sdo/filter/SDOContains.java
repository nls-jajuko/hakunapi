package fi.nls.hakunapi.simple.sdo.filter;

public class SDOContains extends SDOGeometryFunction {

    @Override
    public String getMask() {
        return "CONTAINS+COVERS";
    }

}
