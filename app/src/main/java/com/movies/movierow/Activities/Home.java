package com.movies.movierow.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.movies.movierow.API.Movies;
import com.movies.movierow.Adapters.KidPosterAdapter;
import com.movies.movierow.Adapters.PosterAdapter;
import com.movies.movierow.Adapters.TvShowPosterAdapter;
import com.movies.movierow.Models.KidMoviesModel;
import com.movies.movierow.Models.PopularMoviesModel;
import com.movies.movierow.Models.TvShowModel;
import com.movies.movierow.R;
import com.movies.movierow.Utils.AppConstants;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {
    ImageButton home;
    ImageButton search;
    ImageButton explore_movies;
    ImageButton trending;
    TextView username;
    CircleImageView profile;
    AppCompatButton go_to_quiz;

    TextView today_date;
    private long pressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_home);

        search = findViewById(R.id.search);
        profile = findViewById(R.id.profile_image);
        trending = findViewById(R.id.trending);
        username = findViewById(R.id.username);
        today_date = findViewById(R.id.today_date);
        explore_movies = findViewById(R.id.explore);
        go_to_quiz = findViewById(R.id.quizz);

        SharedPreferences sharedPreferences = getSharedPreferences("User",MODE_APPEND);
        String name = sharedPreferences.getString("name","Explore");

        //set date and time on the home screen
        if(name.equals("Explore")){
            username.setText(name);
        }else
        {
            username.setText("Explore");
        }

        SimpleDateFormat f = new SimpleDateFormat("MMM");
        SimpleDateFormat f1 = new SimpleDateFormat("dd");
        String today="Today, "+f1.format(new Date())+" "+f.format(new Date());
        today_date.setText(today);
        //-------------------

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SearchMovie.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Profile.class));
            }
        });

        trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Trending.class));
            }
        });

        explore_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Explore.class));
            }
        });

        go_to_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Quizz.class));
            }
        });

        getALlTheMoviePoster();

        getAllTheMovieForKids();

        getAllTvShows();

    }

    private void getALlTheMoviePoster(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Movies service = retrofit.create(Movies.class);

        Call<PopularMoviesModel> call = service.getMovies(AppConstants.TMDB_API_KEY,"en-US","popularity.desc",
                false,false,1,"flatrate");
        call.enqueue(new Callback<PopularMoviesModel>() {

            @Override
            public void onResponse(Call<PopularMoviesModel> call, Response<PopularMoviesModel> response) {
                PopularMoviesModel serverResponse = response.body();

                if (response.isSuccessful() && response.body() != null){
                    //Toast.makeText(getApplicationContext(),serverResponse.getResults().get(0).getPoster_path(),Toast.LENGTH_LONG).show();
                    // get the reference of RecyclerView
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movie_poster_view);
                    // set a LinearLayoutManager with default vertical orientation
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,true);
                    linearLayoutManager.setReverseLayout(false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    //  call the constructor of CustomAdapter to send the reference and data to Adapter
                    PosterAdapter customAdapter = new PosterAdapter(serverResponse, getApplicationContext(),Home.this);
                    recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
                }
            }

            @Override
            public void onFailure(Call<PopularMoviesModel> call, Throwable t) {
                 Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
            }

        });
    }

    private void getAllTheMovieForKids(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Movies service = retrofit.create(Movies.class);

        Call<KidMoviesModel> call = service.getKidMovies(AppConstants.TMDB_API_KEY,"US",
                "G",16,true,"popularity.desc");
        call.enqueue(new Callback<KidMoviesModel>() {

            @Override
            public void onResponse(Call<KidMoviesModel> call, Response<KidMoviesModel> response) {
                KidMoviesModel serverResponse = response.body();

                if (response.isSuccessful() && response.body() != null){
                    //Toast.makeText(getApplicationContext(),serverResponse.getResults().get(0).getPoster_path(),Toast.LENGTH_LONG).show();
                    // get the reference of RecyclerView
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.kids_movie_poster_view);
                    // set a LinearLayoutManager with default vertical orientation
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,true);
                    linearLayoutManager.setReverseLayout(false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    //  call the constructor of CustomAdapter to send the reference and data to Adapter
                    KidPosterAdapter customAdapter = new KidPosterAdapter(serverResponse, getApplicationContext(),Home.this);
                    recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
                }
            }

            @Override
            public void onFailure(Call<KidMoviesModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
            }

        });
    }

    private void getAllTvShows(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Movies service = retrofit.create(Movies.class);

        Call<TvShowModel> call = service.getTvShows(AppConstants.TMDB_API_KEY,"en-US",1);
        call.enqueue(new Callback<TvShowModel>() {

            @Override
            public void onResponse(Call<TvShowModel> call, Response<TvShowModel> response) {
                TvShowModel serverResponse = response.body();

                if (response.isSuccessful() && response.body() != null){
                    //Toast.makeText(getApplicationContext(),serverResponse.getResults().get(0).getPoster_path(),Toast.LENGTH_LONG).show();
                    // get the reference of RecyclerView
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tv_shows_poster_view);
                    // set a LinearLayoutManager with default vertical orientation
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,true);
                    linearLayoutManager.setReverseLayout(false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    //  call the constructor of CustomAdapter to send the reference and data to Adapter
                    TvShowPosterAdapter customAdapter = new TvShowPosterAdapter(serverResponse, getApplicationContext(),Home.this);
                    recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
                }
            }

            @Override
            public void onFailure(Call<TvShowModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            this.finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}