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

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BooksActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {
    private static final int BOOK_LOADER_ID = 1;    // Constant value for the book loader ID.

    // Using the ButterKnife library for view injection.
    @BindView(R.id.list)
    ListView bookListView;
    @BindView(R.id.loading_indicator)
    View loadingIndicator;
    @BindView(R.id.empty_view)
    TextView mEmptyStateTextView;
    @BindView(R.id.search_edittext2)
    EditText searchEditText;
    @BindView(R.id.search_button2)
    ImageView searchButton;

    private BookAdapter mAdapter;                   // Adapter for the list of books.
    private String searchString = "";               // String for searching on Google Books.
    private String url = "";                        // Url for getting the JSON document from Google Books.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_activity);
        ButterKnife.bind(this);

        // Get search string from main activity and build search url.
        searchString = getIntent().getExtras().getString("searchString");
        url = getResources().getString(R.string.base_url, searchString);
        searchEditText.setText(searchString);

        // Find a reference to the {@link ListView} in the layout.
        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of books as input.
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView} so the list can be populated in the user
        // interface.
        bookListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected book.
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Create a new intent to view the book URI and send the intent to launch a new
                // activity.
                Uri bookUri = Uri.parse(mAdapter.getItem(position).getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(websiteIntent);
            }
        });

        // If there is a network connection, fetch data
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            getLoaderManager().initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Otherwise, display error.
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        // Set onClick behaviour for the search button.
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        // Set action to perform when "Ok" key is pressed when typing a search string on the edit
        // text.
        searchEditText.setFocusableInTouchMode(true);
        searchEditText.requestFocus();
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button.
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press.
                    search();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Order a new search from the string typed on the edit text.
     */
    void search() {
        searchString = searchEditText.getText().toString();
        if (searchString.isEmpty()) {
            // Search string can not be empty.
            Toast toast = Toast.makeText(getApplicationContext(), R.string.empty_search, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            // Hide the keyboard.
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

            // Restart loader to perform the new search.
            url = getResources().getString(R.string.base_url, searchString);
            mAdapter.clear();
            mEmptyStateTextView.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.VISIBLE);
            getLoaderManager().restartLoader(BOOK_LOADER_ID, null, BooksActivity.this);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL.
        return new BookLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Hide loading indicator because the data has been loaded.
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No results found for..."
        mEmptyStateTextView.setText(getResources().getString(R.string.no_books, searchString));

        // Clear the adapter of previous book data
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's data set.
        // This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    /**
     * Saves the state of the app.
     *
     * @param outState: variable for storing the state of the app.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the ListView state (includes scroll position) as a Parcelable.
        outState.putParcelable("bookListViewState", bookListView.onSaveInstanceState());
    }

    /**
     * Recovers the state of the app, previously saved.
     *
     * @param savedInstanceState: the saved state fot the app.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore previous state (including selected item index and scroll position).
            bookListView.onRestoreInstanceState(savedInstanceState.getParcelable("bookListViewState"));
        }
    }
}
