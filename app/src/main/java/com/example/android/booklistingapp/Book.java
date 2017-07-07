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

/**
 * An {@link Book} object contains information related to a single earthquake.
 */
class Book {
    private String title;           // Title of the book.
    private String authors;         // Authors of the books, comma separated.
    private String publisher;       // Publisher of the book.
    private String publishedDate;   // Publication date of the book.
    private int pages;              // Number of pages of the book.
    private int stars;              // Book rating based on readers' reviews (maximum 5).
    private String description;     // Short description of the book.
    private Bitmap image;           // Bitmap image of the cover of the book.
    private String url;             // Url for the webpage of the book at play.google.com.

    /**
     * Constructs a new {@link Book} object.
     *
     * @param title         is the title of the book.
     * @param authors       is the list of authors.
     * @param publisher     is the publisher of the book.
     * @param publishedDate is the publication date.
     * @param pages         is the number of pages.
     * @param stars         is the rating of the book.
     * @param description   is the short description of the book.
     * @param image         is the bitmap image with the cover of the book.
     * @param url           is the url for the webpage of the book at play.google.com.
     */
    Book(String title, String authors, String publisher, String publishedDate, int pages, int stars, String description, Bitmap image, String url) {
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.pages = pages;
        this.stars = stars;
        this.description = description;
        this.image = image;
        this.url = url;
    }

    /**
     * Getters.
     */
    String getTitle() {
        return title;
    }

    String getAuthors() {
        return authors;
    }

    String getPublisher() {
        return publisher;
    }

    String getPublishedDate() {
        return publishedDate;
    }

    int getPages() {
        return pages;
    }

    int getStars() {
        return stars;
    }

    String getDescription() {
        return description;
    }

    Bitmap getImage() {
        return image;
    }

    String getUrl() {
        return url;
    }
}
