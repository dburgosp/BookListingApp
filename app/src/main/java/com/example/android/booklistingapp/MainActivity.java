package com.example.android.booklistingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set onClick behaviour for the search button.
        Button mainSearchButton = (Button) findViewById(R.id.search_button);
        mainSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchEditText = (EditText) findViewById(R.id.search_edittext);
                String searchString = searchEditText.getText().toString();
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
        });
    }
}
