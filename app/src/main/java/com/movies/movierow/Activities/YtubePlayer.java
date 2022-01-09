package com.movies.movierow.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.movies.movierow.API.Movies;
import com.movies.movierow.Adapters.CastAdapter;
import com.movies.movierow.Adapters.PosterAdapter;
import com.movies.movierow.Adapters.SearchAdapter;
import com.movies.movierow.Models.CastDetails;
import com.movies.movierow.Models.Item;
import com.movies.movierow.Models.PopularMoviesModel;
import com.movies.movierow.Models.Trailer;
import com.movies.movierow.R;
import com.movies.movierow.Utils.AppConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YtubePlayer extends YouTubeBaseActivity {
    YouTubePlayerView ytPlayer;
    Trailer serverResponse;
    CastDetails castResponse;
    @Override
    protected void
    onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ytube_player);

        getVideoId();
        // Get reference to the view of Video player
        ytPlayer = (YouTubePlayerView)findViewById(R.id.ytPlayer);

        //get the cast
        getCast();


    }

    private void getVideoId(){
        String query = getIntent().getStringExtra("query");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.Trailer_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Movies service = retrofit.create(Movies.class);

        Call<Trailer> call = service.getTrailer("snippet",5,query,AppConstants.YoutubeApiKey);
        call.enqueue(new Callback<Trailer>() {

            @Override
            public void onResponse(Call<Trailer> call, Response<Trailer> response) {
                serverResponse = response.body();

                if (response.isSuccessful() && response.body() != null){
                        playVideo();
                }
            }

            @Override
            public void onFailure(Call<Trailer> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
            }

        });
    }

    private void playVideo(){
        Item item = serverResponse.getItems().get(0);
        String movieIdOfYoutube = item.getId().getVideoId();
        ytPlayer.initialize(
                AppConstants.YoutubeApiKey,
                new YouTubePlayer.OnInitializedListener() {
                    // Implement two methods by clicking on red
                    // error bulb inside onInitializationSuccess
                    // method add the video link or the playlist
                    // link that you want to play In here we
                    // also handle the play and pause
                    // functionality
                    @Override
                    public void onInitializationSuccess(
                            YouTubePlayer.Provider provider,
                            YouTubePlayer youTubePlayer, boolean b)
                    {
                        youTubePlayer.loadVideo(movieIdOfYoutube);
                        youTubePlayer.play();
                    }

                    // Inside onInitializationFailure
                    // implement the failure functionality
                    // Here we will show toast
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult
                                                                youTubeInitializationResult)
                    {
                        Toast.makeText(getApplicationContext(), "Video player Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getCast(){
        String type = getIntent().getStringExtra("type");
        int id = getIntent().getIntExtra("movieID",0);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/"+type+"/"+String.valueOf(id)+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Movies service = retrofit.create(Movies.class);

        Call<CastDetails> call = service.getAllCast(AppConstants.TMDB_API_KEY,"en-US");
        call.enqueue(new Callback<CastDetails>() {

            @Override
            public void onResponse(Call<CastDetails> call, Response<CastDetails> response) {
                castResponse = response.body();

                if (response.isSuccessful() && response.body() != null){
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cast_details);
                    // set a LinearLayoutManager with default vertical orientation
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,true);
                    linearLayoutManager.setReverseLayout(false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    //  call the constructor of CustomAdapter to send the reference and data to Adapter
                    CastAdapter customAdapter = new CastAdapter(castResponse, getApplicationContext(),YtubePlayer.this);
                    recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
                }
            }

            @Override
            public void onFailure(Call<CastDetails> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
            }

        });
    }
}