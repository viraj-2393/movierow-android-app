package com.movies.movierow.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.movies.movierow.API.Movies;
import com.movies.movierow.Adapters.TrendAdapter;
import com.movies.movierow.Models.Trends;
import com.movies.movierow.R;
import com.movies.movierow.Utils.AppConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Trending extends AppCompatActivity {
    ImageButton go_back_button;
    private ImageButton home,search,explore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_trending);

        home = findViewById(R.id.home);
        search = findViewById(R.id.search);
        explore = findViewById(R.id.explore);

        //go back to the home page
        go_back_button = findViewById(R.id.go_back_button);
        go_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //go to profile page
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Explore.class));
            }
        });

        //go to home page
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });

        //go to search page
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SearchMovie.class));
            }
        });

        getTrendingActors();
    }

    private void getTrendingActors(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Movies service = retrofit.create(Movies.class);

        Call<Trends> call = service.getTrending(AppConstants.TMDB_API_KEY);
        call.enqueue(new Callback<Trends>() {

            @Override
            public void onResponse(Call<Trends> call, Response<Trends> response) {
                Trends serverResponse = response.body();

                if (response.isSuccessful() && response.body() != null){

                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.trending_faces);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
                    //gridLayoutManager.setReverseLayout(false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    //  call the constructor of CustomAdapter to send the reference and data to Adapter
                    TrendAdapter customAdapter = new TrendAdapter(serverResponse, getApplicationContext(),Trending.this);
                    recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
                }
            }

            @Override
            public void onFailure(Call<Trends> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
            }

        });
    }
}