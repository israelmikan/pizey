package com.clicktheglobe.pizzame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity1 extends AppCompatActivity {
    TextView ratings;
    TextView moviename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);

        String movie=getIntent().getExtras().getString("movie");
        String rating=getIntent().getExtras().getString("rating");


        ratings = (TextView) findViewById(R.id.ratings);
        moviename = (TextView) findViewById(R.id.moviename);

        moviename.setText(movie);
        ratings.setText(rating);

    }
}
