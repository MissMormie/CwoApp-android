package cwoapp.nl.cwoapp.utility;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Sonja on 3/9/2017.
 */

public class NetworkUtils {

    private static final String API_BASE_URL = "http://192.168.1.85:8080";

    public static URL buildUrl(String... path) {
        // COMPLETED (1) Fix this method to return the URL used to query Open Weather Map's API
        Uri.Builder uriBuilder = Uri.parse(API_BASE_URL).buildUpon();
        for (int i = 0; i < path.length; i++) {
            uriBuilder.appendPath(path[i]);
        }
        Uri builtUri = uriBuilder.build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Return server response code.
     *
     * @param url
     * @param jsonString
     * @param requestMethod
     * @return
     * @throws IOException
     */
    public static int uploadToServer(URL url, String jsonString, String requestMethod) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setRequestMethod(requestMethod);

        OutputStream os = conn.getOutputStream();
        os.write(jsonString.getBytes("UTF-8"));
        os.close();

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        int responseCode = conn.getResponseCode();

        conn.disconnect();
        return responseCode;
    }

    /**
     * Connects to server on the given url, Return response code
     *
     * @param url
     * @param requestMethod
     * @return
     * @throws IOException
     */
    public static int sendToServer(URL url, String requestMethod) throws IOException {
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpCon.setRequestMethod(requestMethod);
        httpCon.connect();
        int responseCode = httpCon.getResponseCode();
        return responseCode;
    }
}
