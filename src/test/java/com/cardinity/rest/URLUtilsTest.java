package com.cardinity.rest;

import com.cardinity.Cardinity;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.UUID;

import static org.junit.Assert.*;

public class URLUtilsTest {

    @Test
    public void testConstructorIsPrivate() throws Exception {
        Constructor constructor = URLUtils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        Object o = constructor.newInstance();
        assertNotNull(o);
    }

    @Test
    public void testBuildUrl() throws Exception {
        String url = Cardinity.API_BASE + "/v1/payments";
        assertEquals(url.toLowerCase(), URLUtils.buildUrl());
    }

    @Test
    public void testBuildUrl1() throws Exception {
        UUID paymentId = UUID.randomUUID();
        String url = Cardinity.API_BASE + "/v1/payments/" + paymentId;
        assertEquals(url.toLowerCase(), URLUtils.buildUrl(paymentId));
    }

    @Test
    public void testBuildUrl2() throws Exception {
        UUID paymentId = UUID.randomUUID();
        String url = Cardinity.API_BASE + "/v1/payments/" + paymentId + "/" + RestResource.Resource.REFUNDS;
        assertEquals(url.toLowerCase(), URLUtils.buildUrl(paymentId, RestResource.Resource.REFUNDS));
    }

    @Test
    public void testBuildUrl3() throws Exception {
        UUID paymentId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();

        String url = Cardinity.API_BASE + "/v1/payments/" + paymentId + "/" + RestResource.Resource.REFUNDS + "/" +
                resourceId;
        assertEquals(url.toLowerCase(), URLUtils.buildUrl(paymentId, RestResource.Resource.REFUNDS, resourceId));
    }

    @Test
    public void testFormatURL() throws Exception {
        String url = "https://api.cardinity.com/v1/payments";
        String query = "limit=20";
        assertEquals(url, URLUtils.formatURL(url, null));
        assertEquals(url + "?" + query, URLUtils.formatURL(url, query));
    }

    @Test
    public void testBuildQueryParam() throws Exception {
        assertEquals("limit=20", URLUtils.buildQueryParam("limit", "20"));

    }
}
