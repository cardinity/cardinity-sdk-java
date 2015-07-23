package com.cardinity.oauth;

import com.cardinity.rest.RestResource;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.TreeMap;

public final class CardinityOAuthProvider implements OAuthProvider {

    private static final String OAUTH_VERSION = "1.0";

    private static final SecureRandom RANDOM = new SecureRandom();

    private final OAuthSigner signer;

    private final String consumerKey;
    private final String consumerSecret;

    public CardinityOAuthProvider(String consumerKey, String consumerSecret) {
        this.signer = new HmacOAuthSigner();
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    @Override
    public String buildAuthorizationHeader(RestResource.RequestMethod requestMethod, String requestUrl) throws
            GeneralSecurityException, UnsupportedEncodingException {
        return buildAuthorizationHeader(requestMethod, requestUrl, null);
    }

    @Override
    public String buildAuthorizationHeader(RestResource.RequestMethod requestMethod, String requestUrl, Map<String,
            String> queryParameters) throws GeneralSecurityException, UnsupportedEncodingException {

        String signatureMethod = signer.getSignatureMethod();

        TreeMap<String, String> parameters = new TreeMap<String, String>();

        parameters.put("oauth_consumer_key", consumerKey);
        parameters.put("oauth_nonce", computeNonce());
        parameters.put("oauth_signature_method", signatureMethod);
        parameters.put("oauth_timestamp", computeTimestamp());
        parameters.put("oauth_version", OAUTH_VERSION);

        if (queryParameters != null) {
            for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
                parameters.put(entry.getKey(), entry.getValue());
            }
        }

        StringBuilder parametersBuf = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (first) {
                first = false;
            } else {
                parametersBuf.append('&');
            }
            parametersBuf.append(entry.getKey());
            String value = entry.getValue();
            if (value != null) {
                parametersBuf.append('=').append(value);
            }
        }
        String normalizedParameters = parametersBuf.toString();
        String signatureBaseString = requestMethod + "&" + OAuthUtils.percentEncode(requestUrl) + "&" +
                OAuthUtils.percentEncode(normalizedParameters);
        String signature = signer.computeSignature(signatureBaseString, consumerSecret);
        parameters.put("oauth_signature", signature);

        StringBuilder headerBuf = new StringBuilder("OAuth");
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (entry.getKey().startsWith("oauth_")) appendParameter(headerBuf, entry.getKey(), entry.getValue());
        }

        return headerBuf.substring(0, headerBuf.length() - 1);
    }

    private String computeNonce() {
        return Long.toHexString(Math.abs(RANDOM.nextLong()));
    }

    private String computeTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    private void appendParameter(StringBuilder buf, String name, String value) {
        if (value != null) {
            buf.append(' ').append(OAuthUtils.percentEncode(name)).append("=\"").append(OAuthUtils.percentEncode
                    (value)).append("\",");
        }
    }

}
