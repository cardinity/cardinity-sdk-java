package com.cardinity.rest;

import com.cardinity.Cardinity;
import com.cardinity.exceptions.CardinityClientException;
import com.cardinity.model.CardinityError;
import com.cardinity.model.Response;
import com.cardinity.model.Result;
import com.cardinity.oauth.OAuthProvider;
import com.cardinity.rest.RestResource.RequestMethod;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CardinityRestClient implements RestClient {

    private final OAuthProvider oAuthProvider;

    public CardinityRestClient(OAuthProvider oAuthProvider) {
        this.oAuthProvider = oAuthProvider;
    }

    @Override
    public <T> Result<T> sendRequest(RequestMethod method, String url, TypeToken<T> clazz, T requestObject) {
        return _sendRequest(method, url, clazz, requestObject, null);
    }

    @Override
    public <T> Result<T> sendRequest(RequestMethod method, String url, TypeToken<T> clazz) {
        return _sendRequest(method, url, clazz, null, null);
    }

    @Override
    public <T> Result<T> sendRequest(RequestMethod method, String url, TypeToken<T> clazz, Map<String, String> params) {
        return _sendRequest(method, url, clazz, null, params);
    }

    private <T> Result<T> _sendRequest(RequestMethod method, String url, TypeToken<T> clazz, T requestObject,
            Map<String, String> params) {

        Response response = getResponse(method, url, requestObject, params);

        int responseCode = response.getCode();
        String responseBody = response.getBody();

        Result<T> result;

        if (responseCode < 200 || (responseCode >= 300 && responseCode != 402))
            result = new Result<T>(RestResource.GSON.fromJson(responseBody, CardinityError.class));
        else {
            T resultObject = RestResource.GSON.fromJson(responseBody, clazz.getType());
            result = new Result<T>(resultObject);
        }

        return result;
    }

    private <T> Response getResponse(RequestMethod method, String url, T requestObject, Map<String, String> params) {
        HttpURLConnection conn = null;

        try {
            switch (method) {
                case GET:
                    conn = createGetConnection(url, params, oAuthProvider.buildAuthorizationHeader(method, url,
                            params));
                    break;
                case POST:
                    conn = createPostPatchConnection(url, buildRequestBody(requestObject), true, oAuthProvider
                            .buildAuthorizationHeader(method, url));
                    break;
                case PATCH:
                    conn = createPostPatchConnection(url, buildRequestBody(requestObject), false, oAuthProvider
                            .buildAuthorizationHeader(method, url));
                    break;
                default:
                    throw new CardinityClientException("Unrecognized HTTP request type.");
            }

            int responseCode = conn.getResponseCode();
            String responseBody;

            if (responseCode >= 200 && responseCode < 300) {
                responseBody = getResponseBody(conn.getInputStream());
            } else {
                responseBody = getResponseBody(conn.getErrorStream());
            }

            return new Response(responseCode, responseBody);

        } catch (UnsupportedEncodingException e) {
            throw new CardinityClientException("UnsupportedEncodingException: failed to encode data in ." + Cardinity
                    .ENCODING_CHARSET);
        } catch (IOException e) {
            throw new CardinityClientException("IOException: check connectivity to cardinity servers.");
        } catch (GeneralSecurityException e) {
            throw new CardinityClientException("OAuthException: failed to sign request.");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static HttpURLConnection createGetConnection(String url, Map<String, String> params, String
            authorizationHeader) throws IOException {
        String getURL = createUrl(url, params);
        HttpURLConnection conn = createConnection(getURL);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", authorizationHeader);

        return conn;
    }

    private static HttpURLConnection createPostPatchConnection(String url, String query, Boolean post, String
            authorizationHeader) throws IOException {
        HttpURLConnection conn = createConnection(url);

        conn.setDoOutput(true);
        setRequestMethodBypassingJREMethodLimitation(conn, post ? "POST" : "PATCH");

        conn.setRequestProperty("Content-Type", String.format("application/json;charset=%s", Cardinity
                .ENCODING_CHARSET));
        conn.setRequestProperty("Authorization", authorizationHeader);

        OutputStream output = null;
        try {
            output = conn.getOutputStream();
            output.write(query.getBytes(Cardinity.ENCODING_CHARSET));
        } finally {
            if (output != null) {
                output.close();
            }
        }
        return conn;
    }

    private static HttpURLConnection createConnection(String url) throws IOException {

        URL cardinityURL = new URL(url);

        HttpURLConnection conn = (java.net.HttpURLConnection) cardinityURL.openConnection();
        conn.setConnectTimeout(30 * 1000);
        conn.setReadTimeout(80 * 1000);
        conn.setUseCaches(false);

        for (Map.Entry<String, String> header : getDefaultHeaders().entrySet()) {
            conn.setRequestProperty(header.getKey(), header.getValue());
        }

        return conn;
    }

    private static String getResponseBody(InputStream is) throws IOException {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String body = s.hasNext() ? s.next() : "";
        is.close();

        return body;
    }

    private static Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<String, String>();

        headers.put("Accept-Charset", Cardinity.ENCODING_CHARSET);
        headers.put("Accept", "application/json");
        headers.put("User-Agent", String.format("cardinity-java-client-%s", Cardinity.VERSION));
        headers.put("Cardinity-Version", Cardinity.API_VERSION);

        return headers;
    }

    private static <T> String buildRequestBody(T object) {
        if (object != null) {
            return RestResource.GSON.toJson(object);
        }
        return "";
    }

    private static String createUrl(String url, Map<String, String> params) throws UnsupportedEncodingException {

        if (params == null) return url;

        StringBuilder queryStringBuffer = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (queryStringBuffer.length() > 0) {
                queryStringBuffer.append("&");
            }
            queryStringBuffer.append(URLUtils.buildQueryParam(entry.getKey(), entry.getValue()));
        }
        return URLUtils.formatURL(url, queryStringBuffer.toString());
    }

    /**
     * Author: https://github.com/nacx/jclouds/commit/bfc635668fc95fc4210b25ddec031c703f694b43
     * <p>
     * Workaround for a bug in <code>HttpURLConnection.setRequestMethod(String)</code>
     * The implementation of Sun Microsystems is throwing a <code>ProtocolException</code>
     * when the method is other than the HTTP/1.1 default methods. So
     * to use PATCH and others, we must apply this workaround.
     * </p>
     * See issue http://java.net/jira/browse/JERSEY-639
     */
    private static void setRequestMethodBypassingJREMethodLimitation(final HttpURLConnection httpURLConnection, final
    String method) {
        try {
            httpURLConnection.setRequestMethod(method);
            // If the JRE does not support the given method, set it using reflection
        } catch (final ProtocolException pe) {
            Class<?> connectionClass = httpURLConnection.getClass();
            Field delegateField;
            try {
                // SSL connections may have the HttpURLConnection wrapped inside
                delegateField = connectionClass.getDeclaredField("delegate");
                delegateField.setAccessible(true);
                HttpURLConnection delegateConnection = (HttpURLConnection) delegateField.get(httpURLConnection);
                setRequestMethodBypassingJREMethodLimitation(delegateConnection, method);
            } catch (NoSuchFieldException e) {
                // Ignore for now, keep going
            } catch (IllegalArgumentException e) {
                throw new CardinityClientException("RequestMethodException: failed to set request method.");

            } catch (IllegalAccessException e) {
                throw new CardinityClientException("RequestMethodException: failed to set request method.");

            }
            try {
                Field methodField;
                while (connectionClass != null) {
                    try {
                        methodField = connectionClass.getDeclaredField("method");
                    } catch (NoSuchFieldException e) {
                        connectionClass = connectionClass.getSuperclass();
                        continue;
                    }
                    methodField.setAccessible(true);
                    methodField.set(httpURLConnection, method);
                    break;
                }
            } catch (final Exception e) {
                throw new CardinityClientException("RequestMethodException: failed to set request method.");
            }
        }
    }
}
