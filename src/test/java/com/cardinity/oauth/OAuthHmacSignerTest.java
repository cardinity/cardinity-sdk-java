package com.cardinity.oauth;

import com.cardinity.CardinityBaseTest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OAuthHmacSignerTest extends CardinityBaseTest {

    private static OAuthSigner signer;

    @BeforeClass
    public static void setUpClass() {
        signer = new HmacOAuthSigner();
    }

    @Test
    public void testComputeSignature() throws Exception {

        assertEquals("PxkffxyQh6jsDNcgJ23GpAxs2y8=", signer.computeSignature("justsomerandommessage",
                "yvp0leodf231ihv9u29uuq6w8o4cat9qz2nkvs55oeu833s621"));
    }
}
