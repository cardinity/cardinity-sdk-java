package com.cardinity.oauth;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

public interface OAuthSigner {

    String getSignatureMethod();

    String computeSignature(String signatureBaseString, String consumerSecret) throws GeneralSecurityException,
            UnsupportedEncodingException;
}
