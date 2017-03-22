package cwoapp.nl.cwoapp.utility;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
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
}
