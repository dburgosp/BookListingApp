/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.booklistingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving book data from USGS.
 */
final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Google Books API dataset and return a list of {@link Book} objects.
     *
     * @param requestUrl is the URL for getting the JSON object with the list of books.
     * @return a list of {@link Book} objects.
     */
    static List<Book> fetchBookData(String requestUrl) {
        // Create URL object from the given string URL "requestUrl".
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL.", e);
        }

        // Perform HTTP request to the URL and receive a JSON response back.
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response, create a list of {@link Book}s and
        // return this list.
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     *
     * @param url is the URL for the HTTP request.
     * @return a String as the response.
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        // If the URL is null, then return an empty JSON string.
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200), then read the input stream and
            // parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();

                // Convert the {@link InputStream} into a String which contains the whole JSON
                // response from the server.
                StringBuilder output = new StringBuilder();
                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    String line = reader.readLine();
                    while (line != null) {
                        output.append(line);
                        line = reader.readLine();
                    }
                }
                jsonResponse = output.toString();
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why the
                // makeHttpRequest(URL url) method signature specifies than an IOException could be
                // thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Return a list of {@link Book} objects that has been built up from parsing the given JSON
     * response.
     *
     * @param bookJSON is the JSON object to be parsed and converted to a list of {@link Book}
     *                 objects.
     * @return the list of {@link Book} objects parsed form the input JSON object.
     */
    private static List<Book> extractFeatureFromJson(String bookJSON) {
        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to.
        List<Book> books = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown. Catch the exception so the
        // app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            // Extract the JSONArray associated with the key called "items", which represents a list
            // of items (or books). If there's no "items" array, exit returning null before trying
            // to extract the JSONArray.
            if (baseJsonResponse.isNull("items")) return null;
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            // For each book in the bookArray, create an {@link Book} object.
            JSONObject currentBook;
            for (int i = 0; i < bookArray.length(); i++) {
                // Get a single book at position i within the list of books.
                currentBook = bookArray.getJSONObject(i);

                // For a given book, extract the JSONObject associated with the key called
                // "volumeInfo", which represents a list of all properties for that book.
                if (currentBook.isNull("volumeInfo")) break;
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                // Extract the value for the key called "title", which represents the title of the
                // book.
                String title = "";
                if (!volumeInfo.isNull("title")) title = volumeInfo.getString("title");

                // Extract the JSONArray associated with the key called "authors", which represents
                // the list of authors of the book.
                String authors = "";
                if (!volumeInfo.isNull("authors")) {
                    JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                    for (int j = 0; j < authorsArray.length(); j++) {
                        // Get a single author at position j within the list of authors.
                        if (j == 0) authors = authorsArray.get(j).toString();
                        else authors = authors + ", " + authorsArray.get(j).toString();
                    }
                }

                // Extract the value for the key called "publisher", which represents the publisher
                // of the book.
                String publisher = "";
                if (!volumeInfo.isNull("publisher")) publisher = volumeInfo.getString("publisher");

                // Extract the value for the key called "publishedDate", which represents the
                // publication date of the book.
                String publishedDate = "";
                if (!volumeInfo.isNull("publishedDate"))
                    publishedDate = volumeInfo.getString("publishedDate");

                // Extract the value for the key called "pageCount", which represents the number of
                // pages of the book.
                int pageCount = 0;
                if (!volumeInfo.isNull("pageCount")) pageCount = volumeInfo.getInt("pageCount");

                // Extract the value for the key called "averageRating", which represents the rating
                // of the book based on readers' reviews.
                int averageRating = 0;
                if (!volumeInfo.isNull("averageRating"))
                    averageRating = volumeInfo.getInt("averageRating");

                // Extract the value for the key called "description", which represents the short
                // description of the book.
                String description = "";
                if (!volumeInfo.isNull("description"))
                    description = volumeInfo.getString("description");

                // For a given book, extract the JSONObject associated with the key called
                // "imageLinks", which represents a list of the images associated with that book.
                // Then extract the value for the key called "smallThumbnail", which represents the
                // link to the small image of the cover of the book.
                String smallThumbnail = "";
                Bitmap bmp = null;
                if (!volumeInfo.isNull("imageLinks")) {
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    if (!imageLinks.isNull("smallThumbnail")) {
                        smallThumbnail = imageLinks.getString("smallThumbnail");
                        try {
                            URL url = new URL(smallThumbnail);
                            try {
                                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            } catch (java.io.IOException a) {
                                Log.e("QueryUtils", "Problem getting the bitmap from " + smallThumbnail, a);
                            }
                        } catch (MalformedURLException e) {
                            Log.e("QueryUtils", "Malformed URL eException: " + smallThumbnail, e);
                        }
                    }
                }

                // Extract the value for the key called "canonicalVolumeLink", which represents the
                // url of the book at books.google.com.
                String canonicalVolumeLink = "";
                if (!volumeInfo.isNull("canonicalVolumeLink"))
                    canonicalVolumeLink = volumeInfo.getString("canonicalVolumeLink");

                // Create a new {@link Book} object with the data retrieved from the JSON response.
                Book book = new Book(title, authors, publisher, publishedDate, pageCount, averageRating, description, bmp, canonicalVolumeLink);

                // Add the new {@link Book} to the list of books.
                books.add(book);
            }
        } catch (
                JSONException e)

        {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message with the
            // message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        // Return the list of books.
        return books;
    }
}
