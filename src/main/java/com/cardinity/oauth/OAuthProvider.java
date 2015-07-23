package com.cardinity.oauth;

import com.cardinity.rest.RestResource;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Map;

public interface OAuthProvider {

    String buildAuthorizationHeader(RestResource.RequestMethod requestMethod, String requestUrl) throws
            GeneralSecurityException, UnsupportedEncodingException;

    String buildAuthorizationHeader(RestResource.RequestMethod requestMethod, String requestUrl, Map<String, String>
            params) throws
            GeneralSecurityException, UnsupportedEncodingException;

}
