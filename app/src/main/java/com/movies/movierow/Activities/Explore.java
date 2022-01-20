package com.movies.movierow.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.movies.movierow.API.Movies;
import com.movies.movierow.Adapters.FeedAdapter;
import com.movies.movierow.Adapters.PosterAdapter;
import com.movies.movierow.Adapters.TrendAdapter;
import com.movies.movierow.Models.MovieDetails;
import com.movies.movierow.Models.PopularMoviesModel;
import com.movies.movierow.Models.Trends;
import com.movies.movierow.R;
import com.movies.movierow.Utils.AppConstants;
import com.movies.movierow.Utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Explore extends AppCompatActivity {
    private int page = 2;
    ImageButton go_back_button,search,trending,home;
    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter;
    private ProgressBar loadingPb;
    private List<MovieDetails> listdata = new ArrayList<>();
    boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_explore);

        go_back_button = findViewById(R.id.go_back_button);
        go_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });

        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SearchMovie.class));
            }
        });

        trending = findViewById(R.id.trending);
        trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Trending.class));
            }
        });

        recyclerView = findViewById(R.id.feed_for_you);
        loadingPb = findViewById(R.id.idPBLoading);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,true);
        linearLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        getFeed(page);
        initAdapter();
        initScrollListener();


    }

    private void initAdapter(){
        feedAdapter  = new FeedAdapter(listdata, getApplicationContext(),Explore.this);
        recyclerView.setAdapter(feedAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == listdata.size() - 1) {
                        page++;
                        getFeed(page);
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void getFeed(int page){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Movies service = retrofit.create(Movies.class);
        loadingPb.setVisibility(View.VISIBLE);
        Call<PopularMoviesModel> call = service.getMovies(AppConstants.TMDB_API_KEY,"en-US","popularity.desc",
                false,false,page,"flatrate");
        call.enqueue(new Callback<PopularMoviesModel>() {

            @Override
            public void onResponse(Call<PopularMoviesModel> call, Response<PopularMoviesModel> response) {
                PopularMoviesModel serverResponse = response.body();

                if (response.isSuccessful() && response.body() != null){
                    loadingPb.setVisibility(View.GONE);
                    listdata.addAll(serverResponse.getResults());
                    feedAdapter.notifyDataSetChanged();
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<PopularMoviesModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
            }

        });
    }
}