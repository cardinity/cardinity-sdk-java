package com.cardinity.json;

import com.cardinity.CardinityBaseTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtcDateTypeAdapterTest extends CardinityBaseTest {

    private Gson gson;

    @Before
    public void setUp() throws Exception {

        gson = new GsonBuilder().registerTypeAdapter(Date.class, new UtcDateTypeAdapter()).create();
    }

    @Test
    public void testDeserializeUTCDate() throws Exception {

        Date date = gson.fromJson("\"2014-12-19T11:52:53Z\"", Date.class);
        assertEquals(formatter.parse("2014-12-19T11:52:53Z"), date);
    }

    @Test
    public void testDeserializeUTCDateWithMillis1() throws Exception {

        Date date = gson.fromJson("\"2014-12-19T11:52:53.1Z\"", Date.class);
        assertEquals(formatterWithMillis.parse("2014-12-19T11:52:53.1Z"), date);
    }

    @Test
    public void testDeserializeUTCDateWithMillis2() throws Exception {

        Date date = gson.fromJson("\"2014-12-19T11:52:53.12Z\"", Date.class);
        assertEquals(formatterWithMillis.parse("2014-12-19T11:52:53.12Z"), date);
    }

    @Test
    public void testDeserializeUTCDateWithMillis3() throws Exception {

        Date date = gson.fromJson("\"2014-12-19T11:52:53.123Z\"", Date.class);
        assertEquals(formatterWithMillis.parse("2014-12-19T11:52:53.123Z"), date);
    }

    @Test
    public void testDeserializeOffsetDateWithMillis() throws Exception {

        Date date = gson.fromJson("\"2014-12-19T11:52:53.123+02:00\"", Date.class);
        assertEquals(formatterWithMillisEET.parse("2014-12-19T11:52:53.123"), date);
    }

    @Test
    public void testDeserializeNull() throws Exception {

        assertNull(gson.fromJson((String) null, Date.class));
    }

    @Test
    public void testSerializeUTCDate() throws Exception {
        Date date = formatterWithMillis.parse("2014-12-19T01:52:53.123Z");
        assertEquals("\"2014-12-19T01:52:53.123Z\"", gson.toJson(date, Date.class));
    }

    @Test
    public void testSerializeOffsetDate() throws Exception {
        Date dateEET = formatterWithMillisEET.parse("2014-12-19T01:52:53.123");
        assertEquals("\"2014-12-18T23:52:53.123Z\"", gson.toJson(dateEET, Date.class));
    }


    @Test(expected = JsonParseException.class)
    public void testDeserializeNumberFormatException() throws Exception {

        gson.fromJson("\"2014-12-19T111:52:53Z\"", Date.class);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeNoTimeZoneException() throws Exception {

        gson.fromJson("\"2014-12-19T11:52:53\"", Date.class);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeInvalidTimeZoneException() throws Exception {

        gson.fromJson("\"2014-12-19T11:52:53X\"", Date.class);
    }

}
