package org.avwa.system.authentication.oAuth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletResponse;

public class URLConnClient {

    private static Logger log = LoggerFactory.getLogger(URLConnClient.class);

    public URLConnClient() {
    }

    public String execute(String uri, String method, String body, Map<String, String> headers)
            throws IOException {

        InputStream responseBody = null;
        URLConnection c;
        Map<String, List<String>> responseHeaders = new HashMap<String, List<String>>();
        int responseCode;
        try {
            URL url = new URL(uri);

            c = url.openConnection();
            responseCode = -1;
            if (c instanceof HttpURLConnection) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) c;

                if (headers != null && !headers.isEmpty()) {
                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        httpURLConnection.addRequestProperty(header.getKey(), header.getValue());
                    }
                }

                if (headers != null) {
                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        httpURLConnection.addRequestProperty(header.getKey(), header.getValue());
                    }
                }

                if (method.equals(OAuth.GET)) {
                    httpURLConnection.setRequestMethod(OAuth.GET);
                } else {
                    httpURLConnection.setRequestMethod(method);
                    setRequestBody(body, method, httpURLConnection);
                }

                httpURLConnection.connect();

                InputStream inputStream;
                responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpServletResponse.SC_BAD_REQUEST
                        || responseCode == HttpServletResponse.SC_UNAUTHORIZED) {
                    inputStream = httpURLConnection.getErrorStream();
                } else {
                    inputStream = httpURLConnection.getInputStream();
                }

                responseHeaders = httpURLConnection.getHeaderFields();
                responseBody = inputStream;
            }

            String responseString = null;
            responseString = OAuthUtils.saveStreamAsString(responseBody);
            return responseString;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private void setRequestBody(String body, String requestMethod, HttpURLConnection httpURLConnection)
            throws IOException {
        if (OAuthUtils.isEmpty(body)) {
            return;
        }

        if (OAuth.POST.equals(requestMethod) || OAuth.PUT.equals(requestMethod)) {
            httpURLConnection.setDoOutput(true);
            OutputStream ost = httpURLConnection.getOutputStream();
            PrintWriter pw = new PrintWriter(ost);
            pw.print(body);
            pw.flush();
            pw.close();
        }
    }

}
