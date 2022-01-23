package com.movies.movierow.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.movies.movierow.R;

public class Favorite extends AppCompatActivity {
    ImageButton home;
    ImageButton search;
    ImageButton explore_movies;
    ImageButton trending;
    ImageButton go_back;
    LinearLayout fav_movies;
    LinearLayout fav_tvshows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_favorite);

        search = findViewById(R.id.search);
        trending = findViewById(R.id.trending);
        explore_movies = findViewById(R.id.explore);
        home = findViewById(R.id.home);

        fav_movies = findViewById(R.id.fav_movies);
        fav_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),YourFavs.class);
                intent.putExtra("type","movie");
                startActivity(intent);
            }
        });

        fav_tvshows = findViewById(R.id.fav_tvshow);
        fav_tvshows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),YourFavs.class);
                intent.putExtra("type","tv");
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SearchMovie.class));
            }
        });

        explore_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Explore.class));
            }
        });

        trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Trending.class));
            }
        });

        //go back
        go_back = findViewById(R.id.go_back_button);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}