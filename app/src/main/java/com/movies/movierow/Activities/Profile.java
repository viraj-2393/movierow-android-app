package com.movies.movierow.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.movies.movierow.API.Movies;
import com.movies.movierow.Adapters.PosterAdapter;
import com.movies.movierow.Models.PopularMoviesModel;
import com.movies.movierow.R;
import com.movies.movierow.Utils.MrProjectApp;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {
    ImageButton home;
    ImageButton search;
    TextView profile_username;
    ConstraintLayout user_info;
    AppCompatButton login,logout;
    TextView movies_watched;
    ImageButton go_back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_profile);

        search = findViewById(R.id.search);
        home = findViewById(R.id.home);
        profile_username = findViewById(R.id.profile_username);
        user_info = findViewById(R.id.user_info);
        login = findViewById(R.id.login);
        logout = findViewById(R.id.logout);
        movies_watched = findViewById(R.id.movies_watched);
        go_back_button = findViewById(R.id.go_back_button);

        SharedPreferences sharedPreferences = getSharedPreferences("User",MODE_APPEND);
        String name = sharedPreferences.getString("name","Clara");


        //set date and time on the home screen
        if(name.equals("Clara")){
            user_info.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }else
        {
            profile_username.setText(name.split(" ")[0]);
            movies_watched.setText("Movies Watched: "+MrProjectApp.getMovieID().getMovieIds().size());
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SearchMovie.class));
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        //go back to the home page
        go_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getALlTheMoviePoster();
    }

    private void getALlTheMoviePoster(){
        String movieID;
        Random rand = new Random();
        if(MrProjectApp.getMovieID().getMovieIds().size() != 0) {
            movieID = String.valueOf(MrProjectApp.getMovieID().getMovieIds().get(rand.nextInt(MrProjectApp.getMovieID().getMovieIds().size())));
        }
        else{
            movieID = "512195";
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/"+movieID+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Movies service = retrofit.create(Movies.class);

        Call<PopularMoviesModel> call = service.getSimilarMovies("c09b417853b21948266e85cb76df8365","en-US",1);
        call.enqueue(new Callback<PopularMoviesModel>() {

            @Override
            public void onResponse(Call<PopularMoviesModel> call, Response<PopularMoviesModel> response) {
                PopularMoviesModel serverResponse = response.body();

                if (response.isSuccessful() && response.body() != null){
                    // get the reference of RecyclerView
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.favourite_movies);
                    // set a LinearLayoutManager with default vertical orientation
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,true);
                    linearLayoutManager.setReverseLayout(false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    //  call the constructor of CustomAdapter to send the reference and data to Adapter
                    PosterAdapter customAdapter = new PosterAdapter(serverResponse, getApplicationContext(),Profile.this);
                    recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
                }
            }

            @Override
            public void onFailure(Call<PopularMoviesModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
            }

        });
    }

    public void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Confirm Exit..!!!");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.logout);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Are you sure,You want to exit");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
                settings.edit().clear().apply();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}