package com.movies.movierow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.movies.movierow.Activities.AboutMovie;
import com.movies.movierow.Models.MovieDetails;
import com.movies.movierow.Models.PopularMoviesModel;
import com.movies.movierow.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PosterAdapter extends RecyclerView.Adapter {
   private final Activity activity;
   PopularMoviesModel movies;
   private List<MovieDetails> listdata;
   private Context ctx;
   // private OnFeedItemClickListener onFeedItemClickListener;

    public PosterAdapter(PopularMoviesModel movies, Context ctx, Activity activity) {
        this.movies = movies;
        this.listdata = movies.getResults();
        this.ctx = ctx;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.cardview_poster,parent,false);
        MoviePosterViewHolder MoviePosterViewHolder=new MoviePosterViewHolder(view);
        return MoviePosterViewHolder;
    }

//    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
//        this.onFeedItemClickListener = onFeedItemClickListener;
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MoviePosterViewHolder)holder).bindView(listdata.get(position));
        ((MoviePosterViewHolder) holder).posterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),AboutMovie.class);
                intent.putExtra("position",position);
                intent.putExtra("obj", movies);
                intent.putExtra("Caller","101");
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public interface OnFeedItemClickListener {
        void onEditOffer(View v, int position);
    }

    public class MoviePosterViewHolder extends RecyclerView.ViewHolder
    {

        ImageView posterImage;

        MoviePosterViewHolder(View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.movie_poster);
        }
        void bindView(MovieDetails movies)
        {
            Picasso.get().load("https://image.tmdb.org/t/p/w500"+movies.getPoster_path()).into(posterImage);
        }
    }
}
