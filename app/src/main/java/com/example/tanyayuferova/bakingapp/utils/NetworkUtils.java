package com.example.tanyayuferova.bakingapp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Tanya Yuferova on 11/16/2017.
 */

public class NetworkUtils {

    private static final String RECIPE_DATA_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /**
     * Gets response from RECIPE_DATA_URL
     * @return
     */
    public static String getJsonRecipeData() {
        String result = null;
        try {
            result = getResponseFromHttpUrl(new URL(RECIPE_DATA_URL));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

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
        } finally {
            urlConnection.disconnect();
        }
    }
}
