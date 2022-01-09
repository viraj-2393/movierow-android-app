package com.movies.movierow;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.movies.movierow.API.Movies;
import com.movies.movierow.Activities.AboutMovie;
import com.movies.movierow.Activities.Home;
import com.movies.movierow.Activities.NoInternet;
import com.movies.movierow.Models.MovieID;
import com.movies.movierow.Models.UserModel;
import com.movies.movierow.Utils.MrProjectApp;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView app_name;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);
        app_name = findViewById(R.id.splash_screen_name);
        String name = "<font color=#ffffff>movie</font><font color=#FF0000>ROW</font>";
        app_name.setText(Html.fromHtml(name,Html.FROM_HTML_MODE_LEGACY));

        if(isNetworkAvailable()){
            SharedPreferences sharedPreferences = getSharedPreferences("User",MODE_APPEND);
            String token = sharedPreferences.getString("token","");
            if(token.equals("")){
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // this code will be executed after 2 seconds
                        startActivity(new Intent(getApplicationContext(), Home.class));
                    }
                }, 2000);
            }else {
                isValidToken(token);
            }
        }
        else{
            startActivity(new Intent(getApplicationContext(), NoInternet.class));
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void get_all_movie_ids(String token){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://fast-sierra-40787.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Movies getFav = retrofit.create(Movies.class);

        Call<MovieID> call = getFav.getFavMovie("Bearer "+token);
              call.enqueue(new Callback<MovieID>() {
            @Override
            public void onResponse(Call<MovieID> call, Response<MovieID> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    MrProjectApp.setMovieID(response.body());
                    startActivity(new Intent(getApplicationContext(),Home.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Unable to Reach Server."+response.code(),Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),NoInternet.class));
                }


            }

            @Override
            public void onFailure(Call<MovieID> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),NoInternet.class));
            }
        });
    }

    private void isValidToken(String token){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://fast-sierra-40787.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Movies validate_token = retrofit.create(Movies.class);

        Call<UserModel> call = validate_token.validateToken("Bearer "+token);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    get_all_movie_ids(token);
                }
                else{
                    SharedPreferences settings = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
                    settings.edit().clear().apply();
                    startActivity(new Intent(getApplicationContext(),Home.class));
                }


            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
                settings.edit().clear().apply();
                startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });

    }


}