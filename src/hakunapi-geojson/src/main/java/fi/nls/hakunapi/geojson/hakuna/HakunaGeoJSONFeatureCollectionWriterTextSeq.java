package fi.nls.hakunapi.geojson.hakuna;

import java.io.IOException;

public class HakunaGeoJSONFeatureCollectionWriterTextSeq extends HakunaGeoJSONFeatureCollectionWriterNDJSON {
    
    @Override
    public void startFeature(String fid) throws IOException {
        json.writeRecordSeparator((byte) 0x1e);
        super.startFeature(fid);
    }
    
    @Override
    public void startFeature(long fid) throws IOException {
        json.writeRecordSeparator((byte) 0x1e);
        super.startFeature(fid);
    }
    
}
