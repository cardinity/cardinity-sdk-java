package com.cardinity.oauth;

import com.cardinity.rest.RestResource;
import com.cardinity.rest.URLUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class CardinityOAuthProviderTest {

    private OAuthProvider oAuthProvider;

    @Before
    public void setUp() throws Exception {
        oAuthProvider = new CardinityOAuthProvider("test_key", "test_secret");
    }

    @Test
    public void testBuildAuthorizationHeader() throws Exception {

        String getPaymentUrl = URLUtils.buildUrl(UUID.fromString("dfca64ac-fee6-11e4-a322-1697f925ec7b"));
        String authHeader = oAuthProvider.buildAuthorizationHeader(RestResource.RequestMethod.GET, getPaymentUrl);

        assertNotNull(authHeader);
        assertTrue(authHeader.startsWith("OAuth"));
        assertTrue(authHeader.contains("oauth_consumer_key"));
        assertTrue(authHeader.contains("oauth_signature_method=\"HMAC-SHA1\""));
        assertTrue(authHeader.contains("oauth_timestamp"));
        assertTrue(authHeader.contains("oauth_nonce"));
        assertTrue(authHeader.contains(" oauth_version=\"1.0\""));
        assertTrue(authHeader.contains("oauth_signature"));

    }

    @Test
    public void testBuildAuthorizationHeaderWithParameters() throws Exception {

    }
}
