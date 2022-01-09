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
import com.movies.movierow.Models.MovieDetails;
import com.movies.movierow.Models.PopularMoviesModel;
import com.movies.movierow.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter {
    private final Activity activity;
    PopularMoviesModel movies;
    private List<MovieDetails> listdata;
    private Context ctx;
    // private OnFeedItemClickListener onFeedItemClickListener;

    public FavouriteAdapter(PopularMoviesModel movies, Context ctx, Activity activity) {
        this.movies = movies;
        this.listdata = movies.getResults();
        this.ctx = ctx;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.cardview_poster,parent,false);
        FavouriteAdapter.MovieFavouriteViewHolder MovieFavouriteViewHolder=new FavouriteAdapter.MovieFavouriteViewHolder(view);
        return MovieFavouriteViewHolder;
    }

//    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
//        this.onFeedItemClickListener = onFeedItemClickListener;
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FavouriteAdapter.MovieFavouriteViewHolder)holder).bindView(listdata.get(position));
        ((FavouriteAdapter.MovieFavouriteViewHolder) holder).FavouriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("position",position);
//                bundle.putParcelable("obj",movies);
                Intent intent = new Intent(view.getContext(), AboutMovie.class);
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

    public class MovieFavouriteViewHolder extends RecyclerView.ViewHolder
    {

        ImageView FavouriteImage;

        MovieFavouriteViewHolder(View itemView) {
            super(itemView);
            FavouriteImage = itemView.findViewById(R.id.movie_poster);
        }
        void bindView(MovieDetails movies)
        {
            Picasso.get().load("https://image.tmdb.org/t/p/original"+movies.getPoster_path()).into(FavouriteImage);
        }
    }
}

