package com.movies.movierow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.movies.movierow.Activities.AboutMovie;
import com.movies.movierow.Models.KidMovie;
import com.movies.movierow.Models.KidMoviesModel;
import com.movies.movierow.Models.TvShow;
import com.movies.movierow.Models.TvShowModel;
import com.movies.movierow.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TvShowPosterAdapter extends RecyclerView.Adapter {
    private final Activity activity;
    TvShowModel tvShows;
    private List<TvShow> listdata;
    private Context ctx;
    // private OnFeedItemClickListener onFeedItemClickListener;

    public TvShowPosterAdapter(TvShowModel tvShows, Context ctx, Activity activity) {
        this.tvShows = tvShows;
        this.listdata = tvShows.getResults();
        this.ctx = ctx;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.cardview_poster,parent,false);
        TvShowPosterAdapter.TvShowPosterViewHolder TvShowPosterViewHolder=new TvShowPosterAdapter.TvShowPosterViewHolder(view);
        return TvShowPosterViewHolder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TvShowPosterAdapter.TvShowPosterViewHolder)holder).bindView(listdata.get(position));
        ((TvShowPosterAdapter.TvShowPosterViewHolder) holder).posterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), AboutMovie.class);
                intent.putExtra("position",position);
                intent.putExtra("obj", tvShows);
                intent.putExtra("Caller","103");
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

    public class TvShowPosterViewHolder extends RecyclerView.ViewHolder
    {

        ImageView posterImage;

        TvShowPosterViewHolder(View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.movie_poster);
        }
        void bindView(TvShow tvShow)
        {
            Picasso.get().load("https://image.tmdb.org/t/p/original"+tvShow.getPoster_path()).into(posterImage);
        }
    }
}

