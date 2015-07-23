package com.cardinity.oauth;

import com.cardinity.Cardinity;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

public class HmacOAuthSigner implements OAuthSigner {

    @Override
    public String getSignatureMethod() {
        return "HMAC-SHA1";
    }

    @Override
    public String computeSignature(String signatureBaseString, String consumerSecret) throws
            GeneralSecurityException, UnsupportedEncodingException {

        String key = OAuthUtils.percentEncode(consumerSecret) + "&";
        SecretKey secretKey = new SecretKeySpec(key.getBytes(Cardinity.ENCODING_CHARSET), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        return DatatypeConverter.printBase64Binary(mac.doFinal(signatureBaseString.getBytes(Cardinity
                .ENCODING_CHARSET)));
    }
}
