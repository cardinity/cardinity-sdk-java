package com.cardinity.oauth;

import com.cardinity.Cardinity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class OAuthUtils {

    private OAuthUtils() {
    }

    public static String percentEncode(String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLEncoder.encode(s, Cardinity.ENCODING_CHARSET).replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
