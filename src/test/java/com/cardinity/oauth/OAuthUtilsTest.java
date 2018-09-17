package com.cardinity.oauth;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class OAuthUtilsTest {

    @Test
    public void testConstructorIsPrivate() throws Exception {
        Constructor constructor = OAuthUtils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        Object o = constructor.newInstance();
        assertNotNull(o);
    }

    @Test
    public void testPercentEncode() throws Exception {
        assertEquals("", OAuthUtils.percentEncode(null));
        assertEquals("just%20test%2Bdata%2A", OAuthUtils.percentEncode("just test+data*"));
    }

}
