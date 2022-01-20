package com.movies.movierow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.movies.movierow.Activities.AboutMovie;
import com.movies.movierow.Models.MovieDetails;
import com.movies.movierow.Models.PopularMoviesModel;
import com.movies.movierow.Models.TrendingActors;
import com.movies.movierow.Models.Trends;
import com.movies.movierow.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter {
    private final Activity activity;
    private List<MovieDetails> movies_list;
    private Context ctx;

    public FeedAdapter(List<MovieDetails> movies_list, Context ctx, Activity activity) {
        this.movies_list = movies_list;
        this.ctx = ctx;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.feed_cardview, parent, false);
            FeedAdapter.FeedPosterViewHolder FeedPosterViewHolder = new FeedAdapter.FeedPosterViewHolder(view);
            return FeedPosterViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FeedAdapter.FeedPosterViewHolder) holder).bindView(movies_list.get(position));
        ((FeedAdapter.FeedPosterViewHolder) holder).posterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AboutMovie.class);
                intent.putExtra("position",position);
                intent.putExtra("obj",(Serializable) movies_list);
                intent.putExtra("Caller","EXPL");
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return movies_list.size();
    }


    public interface OnFeedItemClickListener {
        void onEditOffer(View v, int position);
    }

    public class FeedPosterViewHolder extends RecyclerView.ViewHolder
    {

        ImageView posterImage;
        TextView originalName;
        TextView release;

        FeedPosterViewHolder(View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.movie_poster_backdrop);
            originalName = itemView.findViewById(R.id.movie_name_explore);
            release = itemView.findViewById(R.id.release_year_explore);

        }
        void bindView(MovieDetails movieDetails)
        {
            if(movieDetails != null) {
                if(movieDetails.getBackdrop_path() == null){
                    Picasso.get().load(R.drawable.not_found).into(posterImage);
                }else {
                    Picasso.get().load("https://image.tmdb.org/t/p/w780" + movieDetails.getBackdrop_path()).into(posterImage);
                }
                originalName.setText(movieDetails.getTitle());
                release.setText(movieDetails.getRelease_date());
            }
        }
    }



}
