package com.movies.movierow.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.movies.movierow.API.Movies;
import com.movies.movierow.Adapters.PosterAdapter;
import com.movies.movierow.Adapters.SearchAdapter;
import com.movies.movierow.Adapters.SearchTvAdapter;
import com.movies.movierow.Models.PopularMoviesModel;
import com.movies.movierow.Models.TvShowModel;
import com.movies.movierow.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchMovie extends AppCompatActivity {
    SearchView search_movies;
    ImageButton go_back_button;
    ImageView not_found;
    Spinner movie_or_tvshow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_search_movie);

        search_movies = findViewById(R.id.search_movies);
        search_movies.setIconifiedByDefault(false);
        search_movies.setQueryHint("Search...");
        go_back_button = findViewById(R.id.go_back_button);
        not_found = findViewById(R.id.not_found);
        movie_or_tvshow = findViewById(R.id.movie_or_tvshow);

        //set adapter for movie or tvshow picker
        String[] kind = {"Movie","TV Show"};
        ArrayAdapter ad = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,kind);
        movie_or_tvshow.setAdapter(ad);

        //go back to the home page
        go_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(getApplicationContext(),Home.class));
                onBackPressed();
            }
        });

        //search for movies begins here
        search_movies.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(movie_or_tvshow.getSelectedItem().equals("Movie")){
                    getTheMovie(newText);
                }
                else{
                    getTheTv(newText);
                }

                return false;
            }
        });
    }

    private void getTheMovie(String query){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Movies service = retrofit.create(Movies.class);

        Call<PopularMoviesModel> call = service.getSpecificMovie("c09b417853b21948266e85cb76df8365",query);
        call.enqueue(new Callback<PopularMoviesModel>() {

            @Override
            public void onResponse(Call<PopularMoviesModel> call, Response<PopularMoviesModel> response) {
                PopularMoviesModel serverResponse = response.body();
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movie_search_results);
                if(response.body() != null && serverResponse.getResults().size() == 0){
                    //Toast.makeText(getApplicationContext(),"No results found",Toast.LENGTH_LONG).show();
                    not_found.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else if (response.isSuccessful() && response.body() != null){
                    not_found.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    // get the reference of RecyclerView

                    // set a LinearLayoutManager with default vertical orientation
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,true);
                    linearLayoutManager.setReverseLayout(false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    //  call the constructor of CustomAdapter to send the reference and data to Adapter
                    SearchAdapter customAdapter = new SearchAdapter(serverResponse, getApplicationContext(),SearchMovie.this);
                    recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
                }

            }

            @Override
            public void onFailure(Call<PopularMoviesModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
            }

        });
    }

    private void getTheTv(String query){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Movies service = retrofit.create(Movies.class);

        Call<TvShowModel> call = service.getSpecificTv("c09b417853b21948266e85cb76df8365",query);
        call.enqueue(new Callback<TvShowModel>() {

            @Override
            public void onResponse(Call<TvShowModel> call, Response<TvShowModel> response) {
                TvShowModel serverResponse = response.body();
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movie_search_results);
                if(response.body() != null && serverResponse.getResults().size() == 0){
                    //Toast.makeText(getApplicationContext(),"No results found",Toast.LENGTH_LONG).show();
                    not_found.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else if (response.isSuccessful() && response.body() != null){
                    not_found.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    // get the reference of RecyclerView

                    // set a LinearLayoutManager with default vertical orientation
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,true);
                    linearLayoutManager.setReverseLayout(false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    //  call the constructor of CustomAdapter to send the reference and data to Adapter
                    SearchTvAdapter customAdapter = new SearchTvAdapter(serverResponse, getApplicationContext(),SearchMovie.this);
                    recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
                }

            }

            @Override
            public void onFailure(Call<TvShowModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
            }

        });
    }

}