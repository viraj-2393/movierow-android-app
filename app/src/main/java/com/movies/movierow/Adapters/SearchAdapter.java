package com.movies.movierow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.movies.movierow.Activities.AboutMovie;
import com.movies.movierow.Models.MovieDetails;
import com.movies.movierow.Models.PopularMoviesModel;
import com.movies.movierow.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter {
    private final Activity activity;
    PopularMoviesModel movies;
    private List<MovieDetails> listdata;
    private Context ctx;
    // private OnFeedItemClickListener onFeedItemClickListener;

    public SearchAdapter(PopularMoviesModel movies, Context ctx, Activity activity) {
        this.movies = movies;
        this.listdata = movies.getResults();
        this.ctx = ctx;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.search_cardview,parent,false);
        SearchAdapter.MoviePosterViewHolder MoviePosterViewHolder=new SearchAdapter.MoviePosterViewHolder(view);
        return MoviePosterViewHolder;
    }

//    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
//        this.onFeedItemClickListener = onFeedItemClickListener;
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((SearchAdapter.MoviePosterViewHolder) holder).bindView(listdata.get(position));
            ((MoviePosterViewHolder) holder).search_cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), AboutMovie.class);
                    intent.putExtra("position", position);
                    intent.putExtra("obj", movies);
                    intent.putExtra("Caller", "101");
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
        TextView movieTitle;
        TextView releaseYear;
        TextView ratings;
        ConstraintLayout search_cardView;

        MoviePosterViewHolder(View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.searched_movie_poster);
            movieTitle = itemView.findViewById(R.id.searched_movie_name);
            releaseYear = itemView.findViewById(R.id.searched_release_year);
            ratings = itemView.findViewById(R.id.searched_movie_rating);
            search_cardView = itemView.findViewById(R.id.search_cardView);
        }
        void bindView(MovieDetails movies)
        {
            if(movies != null) {
                if(movies.getPoster_path() == null){
                    Picasso.get().load(R.drawable.not_found).into(posterImage);
                }else {
                    Picasso.get().load("https://image.tmdb.org/t/p/w154" + movies.getPoster_path()).into(posterImage);
                }
                movieTitle.setText(movies.getTitle());
                if (movies.getRelease_date() != null && !movies.getRelease_date().equals(""))
                    releaseYear.setText("Release: " + movies.getRelease_date().substring(0, 4));
                ratings.setText("Rating: " + movies.getVote_average());
            }
        }
    }
}

