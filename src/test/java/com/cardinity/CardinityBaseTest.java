package com.cardinity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public abstract class CardinityBaseTest {

    protected final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    protected final static SimpleDateFormat formatterWithMillis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    protected final static SimpleDateFormat formatterWithMillisEET = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    static {
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        formatterWithMillis.setTimeZone(TimeZone.getTimeZone("UTC"));
        formatterWithMillisEET.setTimeZone(TimeZone.getTimeZone("EET"));
    }

    protected String resource(String path) throws IOException {

        InputStream resource = getClass().getResourceAsStream(path);

        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buf = new byte[1024];

        for (int i = resource.read(buf); i > 0; i = resource.read(buf)) {
            os.write(buf, 0, i);
        }

        return os.toString(Cardinity.ENCODING_CHARSET);

    }
}
