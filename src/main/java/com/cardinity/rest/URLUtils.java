package com.cardinity.rest;

import com.cardinity.Cardinity;
import com.cardinity.rest.RestResource.Resource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

public final class URLUtils {

    private final static String URL_SEPARATOR = "/";

    private URLUtils() {

    }

    public static String buildUrl() {
        return buildCardinityUrl(null, null, null);
    }

    public static String buildUrl(UUID paymentId) {
        return buildCardinityUrl(paymentId, null, null);
    }

    public static String buildUrl(UUID paymentId, Resource resource) {
        return buildCardinityUrl(paymentId, resource, null);
    }

    public static String buildUrl(UUID paymentId, Resource resource, UUID actionID) {
        return buildCardinityUrl(paymentId, resource, actionID);
    }

    private static String buildCardinityUrl(UUID paymentId, Resource action, UUID actionId) {

        StringBuilder url = new StringBuilder(Cardinity.API_BASE).append(URL_SEPARATOR).append(Cardinity.API_VERSION)
                .append(URL_SEPARATOR).append(Resource.PAYMENTS);

        if (paymentId != null) {
            url.append(URL_SEPARATOR);
            url.append(paymentId);
            if (action != null) {
                url.append(URL_SEPARATOR);
                url.append(action);
                if (actionId != null) {
                    url.append(URL_SEPARATOR);
                    url.append(actionId);
                }
            }
        }
        return url.toString().toLowerCase();
    }

    public static String formatURL(String url, String query) {
        if (query == null || query.isEmpty()) {
            return url;
        } else {
            String separator = url.contains("?") ? "&" : "?";
            return String.format("%s%s%s", url, separator, query);
        }
    }

    public static String buildQueryParam(String key, String value) throws UnsupportedEncodingException {
        return String.format("%s=%s", URLEncoder.encode(key, Cardinity.ENCODING_CHARSET), URLEncoder.encode(value,
                Cardinity.ENCODING_CHARSET));
    }
}
