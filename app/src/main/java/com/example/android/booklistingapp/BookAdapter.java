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

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An {@link BookAdapter} knows how to create a list item layout for each book in the data source (a
 * list of {@link Book} objects). These list item layouts will be provided to an adapter view like
 * ListView to be displayed to the user.
 */
class BookAdapter extends ArrayAdapter<Book> {
    // Using the ButterKnife library for view injection.
    @BindView(R.id.book_thumbnail)
    ImageView thumbnailImage;
    @BindView(R.id.book_title)
    TextView titleView;
    @BindView(R.id.book_authors)
    TextView authorsView;
    @BindView(R.id.book_publisher_date)
    TextView publicationView;
    @BindView(R.id.book_description)
    TextView descriptionView;

    /**
     * Constructs a new {@link BookAdapter}.
     *
     * @param context of the app.
     * @param books   is the list of books, which is the data source of the adapter.
     */
    BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    /**
     * Returns a list item view that displays information about the book at the given position in
     * the list of books.
     *
     * @param position    is the position of the item within the adapter's data set of the item whose view we want.
     * @param convertView is the old view to reuse, if possible.
     * @param parent      is the  parent that this view will eventually be attached to.
     * @return a View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse.
        // Otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        ButterKnife.bind(this, listItemView);

        // Find the book at the given position in the list of books.
        Book currentBook = getItem(position);

        // Set the image of the book. If there is no image, it will show the default image
        // "default_cover.jpg".
        Bitmap image = currentBook.getImage();
        if (image != null) {
            thumbnailImage.setImageBitmap(image);
        }

        // Set the title of the book. If there is no title, hide the title view.
        String title = currentBook.getTitle();
        if (!title.isEmpty()) titleView.setText(title);
        else titleView.setVisibility(View.GONE);

        // Set the rating section for the book. Given a maximum of 5 stars rating, display stars
        // with index below or equal to the rating of the book and hide stars with index above the
        // rating of the book.
        int rating = currentBook.getStars();
        for (int i = 1; i <= 5; i++) {
            int resource = listItemView.getResources().getIdentifier("book_star" + i, "id", this.getContext().getPackageName());
            ImageView starImageView = (ImageView) listItemView.findViewById(resource);
            if (i <= rating) starImageView.setVisibility(View.VISIBLE);
            else starImageView.setVisibility(View.GONE);
        }

        // Set the list of authors of the book. If there is no artists, hide the artists view.
        String authors = currentBook.getAuthors();
        if (!authors.isEmpty()) authorsView.setText(authors);
        else authorsView.setVisibility(View.GONE);

        // Set the publisher / year of publication of the book.
        String publisher = currentBook.getPublisher();
        String publishedDate = currentBook.getPublishedDate();
        if (!publisher.isEmpty()) {
            if (!publishedDate.isEmpty()) publicationView.setText(publisher + ", " + publishedDate);
            else publicationView.setText(publisher);
        } else {
            if (!publishedDate.isEmpty()) publicationView.setText(publishedDate);
            else publicationView.setVisibility(View.GONE);
        }

        // Set the short description of the book. If there is no short description, hide the
        // description view.
        String description = currentBook.getDescription();
        if (!description.isEmpty()) descriptionView.setText(description);
        else descriptionView.setVisibility(View.GONE);

        // Return the list item view that is now showing the appropriate data.
        return listItemView;
    }
}
