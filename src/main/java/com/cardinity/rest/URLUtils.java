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

    public static String buildUrl(Resource resource) {
        return buildCardinityUrl(resource, null, null, null);
    }

    public static String buildUrl(Resource resource, UUID resourceId) {
        return buildCardinityUrl(resource, resourceId, null, null);
    }

    private static String buildCardinityUrl(UUID paymentId, Resource action, UUID actionId) {
        return buildCardinityUrl(Resource.PAYMENTS, paymentId, action, actionId);
    }

    private static String buildCardinityUrl(Resource resource, UUID resourceId, Resource action, UUID actionId) {
        StringBuilder url = new StringBuilder(Cardinity.API_BASE).append(URL_SEPARATOR).append(Cardinity.API_VERSION)
                .append(URL_SEPARATOR).append(resource.getUrlName());

        if (resourceId != null) {
            url.append(URL_SEPARATOR);
            url.append(resourceId);
            if (action != null) {
                url.append(URL_SEPARATOR);
                url.append(action.getUrlName());
                if (actionId != null) {
                    url.append(URL_SEPARATOR);
                    url.append(actionId);
                }
            }
        }
        return url.toString();
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
