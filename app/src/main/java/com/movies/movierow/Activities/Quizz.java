package com.movies.movierow.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movies.movierow.API.Movies;
import com.movies.movierow.Adapters.PosterAdapter;
import com.movies.movierow.Models.MovieDetails;
import com.movies.movierow.Models.PopularMoviesModel;
import com.movies.movierow.R;
import com.movies.movierow.Utils.AppConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Quizz extends AppCompatActivity {
    private ImageButton go_back;
    private ImageView poster;
    private LinearLayout option_1,option_2,option_3,option_4;
    private TextView opt_1,opt_2,opt_3,opt_4;
    private ImageButton back,forth;
    private int index = 0;
    private List<MovieDetails> movieList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_quizz);

        poster = findViewById(R.id.quiz_poster);
        go_back = findViewById(R.id.go_back_button);
        option_1 = findViewById(R.id.option_1);
        option_2 = findViewById(R.id.option_2);
        option_3 = findViewById(R.id.option_3);
        option_4 = findViewById(R.id.option_4);
        opt_1 = findViewById(R.id.option_1_text);
        opt_2 = findViewById(R.id.option_2_text);
        opt_3 = findViewById(R.id.option_3_text);
        opt_4 = findViewById(R.id.option_4_text);
        back = findViewById(R.id.backward);
        forth = findViewById(R.id.forward);

        option_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option_1.setBackgroundResource(R.drawable.selected_border);
                option_2.setBackgroundResource(R.drawable.option_border);
                option_3.setBackgroundResource(R.drawable.option_border);
                option_4.setBackgroundResource(R.drawable.option_border);
            }
        });

        option_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option_1.setBackgroundResource(R.drawable.option_border);
                option_2.setBackgroundResource(R.drawable.selected_border);
                option_3.setBackgroundResource(R.drawable.option_border);
                option_4.setBackgroundResource(R.drawable.option_border);
            }
        });

        option_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option_1.setBackgroundResource(R.drawable.option_border);
                option_2.setBackgroundResource(R.drawable.option_border);
                option_3.setBackgroundResource(R.drawable.selected_border);
                option_4.setBackgroundResource(R.drawable.option_border);
            }
        });

        option_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option_1.setBackgroundResource(R.drawable.option_border);
                option_2.setBackgroundResource(R.drawable.option_border);
                option_3.setBackgroundResource(R.drawable.option_border);
                option_4.setBackgroundResource(R.drawable.selected_border);
            }
        });

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPoster(--index);
            }
        });
        forth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPoster(++index);
            }
        });

        getALlTheMoviePoster();
    }

    private void getALlTheMoviePoster(){
        Random random = new Random();
        int page = random.nextInt(100);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Movies service = retrofit.create(Movies.class);

        Call<PopularMoviesModel> call = service.getMovies(AppConstants.TMDB_API_KEY,"en-US","popularity.desc",
                false,false,page,"flatrate");
        call.enqueue(new Callback<PopularMoviesModel>() {

            @Override
            public void onResponse(Call<PopularMoviesModel> call, Response<PopularMoviesModel> response) {
                PopularMoviesModel serverResponse = response.body();

                if (response.isSuccessful() && response.body() != null){
                    movieList.addAll(serverResponse.getResults());
                    displayPoster(index);
                }
            }

            @Override
            public void onFailure(Call<PopularMoviesModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
            }

        });
    }

    private void displayPoster(int index){
        MovieDetails movieDetails = movieList.get(index);
        Picasso.get().load("https://image.tmdb.org/t/p/w780" + movieDetails.getBackdrop_path()).into(poster);

        Random random = new Random();
        int option = random.nextInt(4);
        switch (option){
            case 0: opt_1.setText("A) "+movieDetails.getTitle());break;
            case 1: opt_2.setText("B) "+movieDetails.getTitle());break;
            case 2: opt_3.setText("C) "+movieDetails.getTitle());break;
            default: opt_4.setText("D) "+movieDetails.getTitle());break;
        }
    }

}