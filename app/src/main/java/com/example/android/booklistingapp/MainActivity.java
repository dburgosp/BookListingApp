package com.example.android.booklistingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    // Using the ButterKnife library for view injection.
    @BindView(R.id.search_edittext)
    EditText searchEditText;
    @BindView(R.id.search_button)
    Button searchButton;
    private String searchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
     * Order a search from the string typed on the edit text.
     */
    void search() {
        searchString = searchEditText.getText().toString();
        if (searchString.isEmpty()) {
            // Search string can not be empty.
            Toast toast = Toast.makeText(getApplicationContext(), R.string.empty_search, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            // Open BooksActivity for performing the search and displaying results.
            Intent intent = new Intent(MainActivity.this, BooksActivity.class);
            intent.putExtra("searchString", searchString);
            startActivity(intent);
        }
    }
}
