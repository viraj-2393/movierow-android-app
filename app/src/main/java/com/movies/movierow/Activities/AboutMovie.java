package com.movies.movierow.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewManager;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.movies.movierow.API.Movies;
import com.movies.movierow.Adapters.SearchTvAdapter;
import com.movies.movierow.Controllers.ViewModal;
import com.movies.movierow.Models.FavModal;
import com.movies.movierow.Models.KidMovie;
import com.movies.movierow.Models.KidMoviesModel;
import com.movies.movierow.Models.MovieDetails;
import com.movies.movierow.Models.MovieID;
import com.movies.movierow.Models.PopularMoviesModel;
import com.movies.movierow.Models.TvShow;
import com.movies.movierow.Models.TvShowModel;
import com.movies.movierow.Models.UserModel;
import com.movies.movierow.R;
import com.movies.movierow.Utils.MrProjectApp;
import com.squareup.picasso.Picasso;

import java.io.PipedOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AboutMovie extends AppCompatActivity {
    private ImageView posterImage;
    private TextView movie_name;
    private TextView movie_description;
    private TextView movie_voteAverage;
    private TextView release_year;
    private TextView movie_language;
    private ImageButton go_back;
    private ImageView heart_button;
    private ImageButton home,search,explore,trending_page,fav_page;
    private boolean is_fav = false;
    private int movieId;
    LinearLayout watch_trailer;
    String query = "";
    String year = "";
    String type = "";
    String description = "";
    String language = "";
    String rating = "";
    String img_path = "";
    private FavModal favModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_about_movie);

        posterImage = findViewById(R.id.movie_poster_large);
        movie_name = findViewById(R.id.movie_name);
        movie_description = findViewById(R.id.description);
        movie_voteAverage = findViewById(R.id.vote_average);
        release_year = findViewById(R.id.release_year);
        movie_language = findViewById(R.id.movie_language);
        go_back = findViewById(R.id.go_back);
        heart_button = findViewById(R.id.heart_button);
        watch_trailer = findViewById(R.id.watch_trailer);
        home = findViewById(R.id.home);
        search = findViewById(R.id.search);
        explore = findViewById(R.id.explore);
        trending_page = findViewById(R.id.trending);




        //go back
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //set a movie as favourite
        heart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!is_fav) {
                    FavModal favModal = new FavModal(movieId, query, description, img_path, rating, year, language, type);
                    ViewModal viewModal = new ViewModal(getApplication(), "", 0);
                    viewModal.insert(favModal);
                    Toast.makeText(getApplicationContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                    heart_button.setImageResource(R.drawable.heart);
                    is_fav = true;

                }
                else{
                    ViewModal viewModal = new ViewModal(getApplication(), "", movieId);
                    FavModal favModal = viewModal.getFav(movieId);
                    viewModal.delete(favModal);
                    Toast.makeText(getApplicationContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
                    heart_button.setImageResource(R.drawable.cold_heart);
                    is_fav = false;
                }
            }
        });

        //watch a trailer
        watch_trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),YtubePlayer.class);
                intent.putExtra("query",query+" trailer "+year);
                intent.putExtra("movieID",movieId);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });

        //go to explore page
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Explore.class));
            }
        });

        //go to trending page
        trending_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Trending.class));
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

        fav_page = findViewById(R.id.fav_page);
        fav_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Favorite.class));
            }
        });

        movie_description.setMovementMethod(new ScrollingMovementMethod());

        String caller = getIntent().getStringExtra("Caller");
        if(caller.equals("101")){
            popularMovies();
        }
        else if(caller.equals("102")){
            kidMovies();
        }
        else if(caller.equals("EXPL")){
            exploreMovies();
        }
        else if(caller.equals("FAV")){
            favMovies();
        }
        else{
            TvShow();
        }

        //check if the movie is favorite or not
        ViewModal viewModal = new ViewModal(getApplication(),"",movieId);
        favModal = viewModal.getFav(movieId);
        if(favModal != null){
            is_fav = true;
            heart_button.setImageResource(R.drawable.heart);
        }

    }

    private void favMovies() {
        FavModal favModal = (FavModal) getIntent().getSerializableExtra("obj");
        type = favModal.getType();
        int index = getIntent().getIntExtra("position", 0);
        img_path = favModal.getPoster_path();
        movieId = favModal.getMovieID(); //Get Movie Id to set it as favourite
        //check if the movie is favourite
        MovieID mID = MrProjectApp.getMovieID();
        if (mID.getMovieIds().contains(movieId)) {
            heart_button.setImageResource(R.drawable.heart);
        } else {
            heart_button.setImageResource(R.drawable.cold_heart);
        }

        query = favModal.getName();
        if (favModal.getYear() != null) year = favModal.getYear();//.substring(0,4);
        if (favModal != null) {
            if (favModal.getPoster_path() == null) {
                Picasso.get().load(R.drawable.not_found).into(posterImage);
            } else {
                Picasso.get().load("https://image.tmdb.org/t/p/original" + img_path).into(posterImage);
            }
            movie_name.setText(query);
            //set movie description
            movie_description.setText(favModal.getDescription());
            description = favModal.getDescription();
            //--------------

            //set rating
            movie_voteAverage.setText(String.valueOf(favModal.getRating()));
            rating = String.valueOf(favModal.getRating());
            //--------------
            if (favModal.getYear() != null && !favModal.getYear().equals("")) {
                release_year.setText(favModal.getYear());
            }

            //set language
            movie_language.setText(favModal.getLanguage().toUpperCase());
            language = favModal.getLanguage().toUpperCase();
            //------------
        }
    }

    private void exploreMovies(){
        List<MovieDetails> movies = (List<MovieDetails>)getIntent().getSerializableExtra("obj");
        type = "movie";
        int index = getIntent().getIntExtra("position",0);
        img_path = movies.get(index).getPoster_path();
        movieId = movies.get(index).getId(); //Get Movie Id to set it as favourite
        //check if the movie is favourite
        MovieID mID = MrProjectApp.getMovieID();
        if(mID.getMovieIds().contains(movieId)){
            heart_button.setImageResource(R.drawable.heart);
        }
        else{
            heart_button.setImageResource(R.drawable.cold_heart);
        }

        MovieDetails movieDetails = movies.get(index);
        query = movieDetails.getTitle();
        if(movieDetails.getRelease_date() != null) year = movieDetails.getRelease_date().substring(0,4);
        if(movieDetails != null){
            if(movieDetails.getPoster_path() == null){
                Picasso.get().load(R.drawable.not_found).into(posterImage);
            }else {
                Picasso.get().load("https://image.tmdb.org/t/p/original" + img_path).into(posterImage);
            }
            movie_name.setText(query);
            //set movie description
            movie_description.setText(movieDetails.getOverview());
            description = movieDetails.getOverview();
            //--------------

            //set rating
            movie_voteAverage.setText(String.valueOf(movieDetails.getVote_average()));
            rating = String.valueOf(movieDetails.getVote_average());
            //--------------
            if(movieDetails.getRelease_date() != null && !movieDetails.getRelease_date().equals("")){
                release_year.setText(movieDetails.getRelease_date().substring(0,4));
            }

            //set language
            movie_language.setText(movieDetails.getOriginal_language().toUpperCase());
            language = movieDetails.getOriginal_language().toUpperCase();
            //------------
        }
    }

    private void popularMovies(){
        PopularMoviesModel model = (PopularMoviesModel)getIntent().getSerializableExtra("obj");
        type = "movie";
        int index = getIntent().getIntExtra("position",0);
        img_path = model.getResults().get(index).getPoster_path();
        movieId = model.getResults().get(index).getId(); //Get Movie Id to set it as favourite
        //check if the movie is favourite
        MovieID mID = MrProjectApp.getMovieID();
        if(mID.getMovieIds().contains(movieId)){
            heart_button.setImageResource(R.drawable.heart);
        }
        else{
            heart_button.setImageResource(R.drawable.cold_heart);
        }

        MovieDetails movieDetails = model.getResults().get(index);
        query = movieDetails.getTitle();
        if(movieDetails.getRelease_date() != null) year = movieDetails.getRelease_date().substring(0,4);
        if(model != null){
            if(movieDetails.getPoster_path() == null){
                Picasso.get().load(R.drawable.not_found).into(posterImage);
            }else {
                Picasso.get().load("https://image.tmdb.org/t/p/original" + img_path).into(posterImage);
            }
            movie_name.setText(query);
            //set movie description
            movie_description.setText(movieDetails.getOverview());
            description = movieDetails.getOverview();
            //--------------

            //set rating
            movie_voteAverage.setText(String.valueOf(movieDetails.getVote_average()));
            rating = String.valueOf(movieDetails.getVote_average());
            //--------------
            if(movieDetails.getRelease_date() != null && !movieDetails.getRelease_date().equals("")){
                release_year.setText(movieDetails.getRelease_date().substring(0,4));
            }

            //set language
            movie_language.setText(movieDetails.getOriginal_language().toUpperCase());
            language = movieDetails.getOriginal_language().toUpperCase();
            //------------
        }

    }

    private void kidMovies(){
        KidMoviesModel model = (KidMoviesModel)getIntent().getSerializableExtra("obj");
        type = "movie";
        int index = getIntent().getIntExtra("position",0);
        img_path = model.getResults().get(index).getPoster_path();
        movieId = model.getResults().get(index).getId(); //Get Movie Id to set it as favourite
        //check if the movie is favourite
        MovieID mID = MrProjectApp.getMovieID();
        if(mID.getMovieIds().contains(movieId)){
            heart_button.setImageResource(R.drawable.heart);
        }
        else{
            heart_button.setImageResource(R.drawable.cold_heart);
        }

        KidMovie movieDetails = model.getResults().get(index);
        query = movieDetails.getTitle();
        if(movieDetails.getRelease_date() != null){
            year = movieDetails.getRelease_date().substring(0,4);
        }
        if(model != null){
            if(movieDetails.getPoster_path() == null){
                Picasso.get().load(R.drawable.not_found).into(posterImage);
            }else {
                Picasso.get().load("https://image.tmdb.org/t/p/original" + img_path).into(posterImage);
            }
            movie_name.setText(movieDetails.getTitle());
            //set movie description
            movie_description.setText(movieDetails.getOverview());
            description = movieDetails.getOverview();
            //--------------

            //set rating
            movie_voteAverage.setText(String.valueOf(movieDetails.getVote_average()));
            rating = String.valueOf(movieDetails.getVote_average());
            //--------------
            if(movieDetails.getRelease_date() != null && !movieDetails.getRelease_date().equals("")){
                release_year.setText(movieDetails.getRelease_date().substring(0,4));
            }

            //set language
            movie_language.setText(movieDetails.getOriginal_language().toUpperCase());
            language = movieDetails.getOriginal_language().toUpperCase();
            //------------
        }
    }

    private void TvShow(){
        TvShowModel model = (TvShowModel)getIntent().getSerializableExtra("obj");
        type = "tv";
        int index = getIntent().getIntExtra("position",0);
        img_path = model.getResults().get(index).getPoster_path();
        movieId = model.getResults().get(index).getId(); //Get Movie Id to set it as favourite
        //check if the movie is favourite
        MovieID mID = MrProjectApp.getMovieID();
        if(mID.getMovieIds().contains(movieId)){
            heart_button.setImageResource(R.drawable.heart);
        }
        else{
            heart_button.setImageResource(R.drawable.cold_heart);
        }

        TvShow tvShowDetails = model.getResults().get(index);
        query = tvShowDetails.getName();
        if(tvShowDetails.getFirst_air_date() != null && !tvShowDetails.getFirst_air_date().equals("")) year = tvShowDetails.getFirst_air_date().substring(0,4);
        if(model != null){
            if(tvShowDetails.getPoster_path() == null){
                Picasso.get().load(R.drawable.not_found).into(posterImage);
            }else {
                Picasso.get().load("https://image.tmdb.org/t/p/original" + img_path).into(posterImage);
            }
            movie_name.setText(tvShowDetails.getName());
            //set movie description
            movie_description.setText(tvShowDetails.getOverview());
            description = tvShowDetails.getOverview();
            //--------------

            //set rating
            movie_voteAverage.setText(String.valueOf(tvShowDetails.getVote_average()));
            rating = String.valueOf(tvShowDetails.getVote_average());
            //--------------
            if(tvShowDetails.getFirst_air_date() != null && !tvShowDetails.getFirst_air_date().equals("")){
                release_year.setText(tvShowDetails.getFirst_air_date().substring(0,4));
            }

            //set language
            movie_language.setText(tvShowDetails.getOriginal_language().toUpperCase());
            language = tvShowDetails.getOriginal_language().toUpperCase();
            //------------
        }
    }

    private void setFavMovie(String token,int movieId){
        //JsonArray jsonArray = new JsonArray();
        //jsonArray.add(new JsonPrimitive(movieId));
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("movieId", movieId);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://fast-sierra-40787.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Movies setFav = retrofit.create(Movies.class);

        Call<Object> call = setFav.setFavMovie("Bearer "+token,jsonObject);
        final ProgressDialog dialog = ProgressDialog.show(AboutMovie.this, "",
                getResources().getString(R.string.loading), true);
        dialog.setCancelable(false);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.code() == 200) {
                    heart_button.setImageResource(R.drawable.heart);
                    MrProjectApp.getMovieID().getMovieIds().add(movieId);
                    Toast.makeText(getBaseContext(), "Added to favourites!", Toast.LENGTH_SHORT).show();
                }
                else if(response.isSuccessful() && response.code() == 204){
                    heart_button.setImageResource(R.drawable.cold_heart);
                    MrProjectApp.getMovieID().getMovieIds().remove(Integer.valueOf(movieId));
                    Toast.makeText(getBaseContext(), "Removed from favourites!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Sign in! "+response.code(),Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}