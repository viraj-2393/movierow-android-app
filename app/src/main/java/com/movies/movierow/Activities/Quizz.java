package com.movies.movierow.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movies.movierow.API.Movies;
import com.movies.movierow.Adapters.PosterAdapter;
import com.movies.movierow.MainActivity;
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
    private LinearLayout option_1,option_2,option_3,option_4,player_life;
    private TextView opt_1,opt_2,opt_3,opt_4,score,high_sc;
    private ImageButton back,forth;
    private int index = 0;
    private int correct_option = 0;
    private int player_score = 0;
    private int life_left = 4;
    private int high_score;
    private int page = 5;
    private List<MovieDetails> movieList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_quizz);

        SharedPreferences sharedPreferences = getSharedPreferences("Score",MODE_APPEND);
        high_score = Integer.parseInt(sharedPreferences.getString("score","0"));

        high_sc = findViewById(R.id.high_score);
        high_sc.setText("High Score: "+String.valueOf(high_score));


        poster = findViewById(R.id.quiz_poster);
        go_back = findViewById(R.id.go_back_button);
        option_1 = findViewById(R.id.option_1);
        option_2 = findViewById(R.id.option_2);
        option_3 = findViewById(R.id.option_3);
        option_4 = findViewById(R.id.option_4);
        player_life = findViewById(R.id.player_life);
        opt_1 = findViewById(R.id.option_1_text);
        opt_2 = findViewById(R.id.option_2_text);
        opt_3 = findViewById(R.id.option_3_text);
        opt_4 = findViewById(R.id.option_4_text);
        score = findViewById(R.id.score);
        back = findViewById(R.id.backward);
        forth = findViewById(R.id.forward);

        //set forth to disabled
        forth.setBackgroundResource(R.drawable.option_border);
        forth.setEnabled(false);


        //random page
        Random random = new Random();
        page = random.nextInt(100);


        option_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableOptions();
                optionSelector(1);
                forth.setBackgroundResource(R.drawable.back_forth);
                forth.setEnabled(true);

                if(movieList.get(index).getTitle().equals(opt_1.getText().toString().substring(3))){
                    option_1.setBackgroundResource(R.drawable.selected_border);
                    player_score++;
                    score.setText("Score: "+String.valueOf(player_score));
                }
                else{
                    option_1.setBackgroundResource(R.drawable.rejected_border);
                    decreaseLife();
                }
            }
        });

        option_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableOptions();
                optionSelector(2);
                forth.setBackgroundResource(R.drawable.back_forth);
                forth.setEnabled(true);

                if(movieList.get(index).getTitle().equals(opt_2.getText().toString().substring(3))){
                    player_score++;
                    score.setText("Score: "+String.valueOf(player_score));
                    option_2.setBackgroundResource(R.drawable.selected_border);
                }
                else{
                    option_2.setBackgroundResource(R.drawable.rejected_border);
                    decreaseLife();
                }
            }
        });

        option_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableOptions();
                optionSelector(3);
                forth.setBackgroundResource(R.drawable.back_forth);
                forth.setEnabled(true);

                if(movieList.get(index).getTitle().equals(opt_3.getText().toString().substring(3))){
                    player_score++;
                    score.setText("Score: "+String.valueOf(player_score));
                    option_3.setBackgroundResource(R.drawable.selected_border);
                }
                else{
                    option_3.setBackgroundResource(R.drawable.rejected_border);
                    decreaseLife();
                }
            }
        });

        option_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableOptions();
                optionSelector(4);
                forth.setBackgroundResource(R.drawable.back_forth);
                forth.setEnabled(true);

                if(movieList.get(index).getTitle().equals(opt_4.getText().toString().substring(3))){
                    player_score++;
                    score.setText("Score: "+String.valueOf(player_score));
                    option_4.setBackgroundResource(R.drawable.selected_border);
                }
                else{
                    option_4.setBackgroundResource(R.drawable.rejected_border);
                    decreaseLife();
                }
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
                --index;
                displayPoster();
            }
        });
        forth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //allow user to click on the options
                enableOptions();
                optionSelector(0);
                ++index;
                displayPoster();
                forth.setBackgroundResource(R.drawable.option_border);
                forth.setEnabled(false);
            }
        });

        getALlTheMoviePoster();
    }

    private void getALlTheMoviePoster(){

        Random random = new Random();
        int pg = random.nextInt(100);
        while(page == pg){
            pg = random.nextInt(100);
        }
        page = pg;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Movies service = retrofit.create(Movies.class);

        Call<PopularMoviesModel> call = service.getMovies(AppConstants.TMDB_API_KEY,"en-US","popularity.desc",
                false,false,pg,"flatrate");
        call.enqueue(new Callback<PopularMoviesModel>() {

            @Override
            public void onResponse(Call<PopularMoviesModel> call, Response<PopularMoviesModel> response) {
                PopularMoviesModel serverResponse = response.body();

                if (response.isSuccessful() && response.body() != null){
                    page++;
                    movieList.addAll(serverResponse.getResults());
                    displayPoster();
                }
            }

            @Override
            public void onFailure(Call<PopularMoviesModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
            }

        });
    }

    private void displayPoster(){
        if((index+1) >= movieList.size()){
            getALlTheMoviePoster();
            return;
        }
        else if(index < 0){index++;return;}
        MovieDetails movieDetails = movieList.get(index);
        if(movieDetails.getBackdrop_path().equals("")){movieDetails = movieList.get(index++);}
        Picasso.get().load("https://image.tmdb.org/t/p/w780" + movieDetails.getBackdrop_path()).into(poster);

        Random random = new Random();
        int option = random.nextInt(4);
        //set all random titles
        opt_1.setText("A) "+movieList.get(random.nextInt(movieList.size())).getTitle());
        opt_2.setText("B) "+movieList.get(random.nextInt(movieList.size())).getTitle());
        opt_3.setText("C) "+movieList.get(random.nextInt(movieList.size())).getTitle());
        opt_4.setText("D) "+movieList.get(random.nextInt(movieList.size())).getTitle());
        //-----------
        switch (option){
            case 0: opt_1.setText("A) "+movieDetails.getTitle());break;
            case 1: opt_2.setText("B) "+movieDetails.getTitle());break;
            case 2: opt_3.setText("C) "+movieDetails.getTitle());break;
            default: opt_4.setText("D) "+movieDetails.getTitle());break;
        }

    }

    private void disableOptions(){
        option_1.setClickable(false);
        option_2.setClickable(false);
        option_3.setClickable(false);
        option_4.setClickable(false);
    }
    private void enableOptions(){
        option_1.setClickable(true);
        option_2.setClickable(true);
        option_3.setClickable(true);
        option_4.setClickable(true);
    }

    private void optionSelector(int opt){
        option_1.setBackgroundResource(R.drawable.option_border);
        option_2.setBackgroundResource(R.drawable.option_border);
        option_3.setBackgroundResource(R.drawable.option_border);
        option_4.setBackgroundResource(R.drawable.option_border);

        switch(opt){
            case 1: option_1.setBackgroundResource(R.drawable.selected_border);break;
            case 2: option_2.setBackgroundResource(R.drawable.selected_border);break;
            case 3: option_3.setBackgroundResource(R.drawable.selected_border);break;
            case 4: option_4.setBackgroundResource(R.drawable.selected_border);break;
            default:break;
        }
    }

    private void decreaseLife(){
        if(--life_left <= 0){
            stillContinue();
            return;
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) player_life.getLayoutParams();
        params.weight = life_left;
        player_life.setLayoutParams(params);
    }

    private void increaseLife(){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) player_life.getLayoutParams();
        params.weight = life_left;
        player_life.setLayoutParams(params);
    }

    private void stillContinue(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Play Again..!!!");
        alertDialogBuilder.setIcon(R.drawable.replay);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if(player_score > high_score) {
                    SharedPreferences settings = getApplicationContext().getSharedPreferences("Score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("score",String.valueOf(player_score));
                    editor.apply();
                    high_sc.setText("High Score: "+player_score);
                }
                life_left = 4;
                player_score = 0;
                increaseLife();
                optionSelector(0);
                enableOptions();
                score.setText("Score: 0");

            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(player_score > high_score) {
                    SharedPreferences settings = getApplicationContext().getSharedPreferences("Score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("score",String.valueOf(player_score));
                    editor.apply();
                }
               startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("EXIT..???");
        alertDialogBuilder.setIcon(R.drawable.logout);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if(player_score > high_score) {
                    SharedPreferences settings = getApplicationContext().getSharedPreferences("Score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("score",String.valueOf(player_score));
                    editor.apply();
                }
                startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(player_score > high_score) {
                    SharedPreferences settings = getApplicationContext().getSharedPreferences("Score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("score",String.valueOf(player_score));
                    editor.apply();
                }

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