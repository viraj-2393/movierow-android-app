package com.movies.movierow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.movies.movierow.Activities.AboutMovie;
import com.movies.movierow.Models.MovieDetails;
import com.movies.movierow.Models.TvShow;
import com.movies.movierow.Models.TvShowModel;
import com.movies.movierow.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchTvAdapter extends RecyclerView.Adapter {
private final Activity activity;
        TvShowModel tvShowModel;
private List<TvShow> listdata;
private Context ctx;
// private OnFeedItemClickListener onFeedItemClickListener;

public SearchTvAdapter(TvShowModel tvShowModel, Context ctx, Activity activity) {
        this.tvShowModel = tvShowModel;
        this.listdata = tvShowModel.getResults();
        this.ctx = ctx;
        this.activity = activity;
        }

@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.search_cardview,parent,false);
        SearchTvAdapter.TvPosterViewHolder TvPosterViewHolder=new SearchTvAdapter.TvPosterViewHolder(view);
        return TvPosterViewHolder;
        }

//    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
//        this.onFeedItemClickListener = onFeedItemClickListener;
//    }

@Override
public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SearchTvAdapter.TvPosterViewHolder) holder).bindView(listdata.get(position));
        ((TvPosterViewHolder) holder).search_cardView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), AboutMovie.class);
        intent.putExtra("position", position);
        intent.putExtra("obj", tvShowModel);
        intent.putExtra("Caller", "103");
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

public class TvPosterViewHolder extends RecyclerView.ViewHolder
{

    ImageView posterImage;
    TextView tvShowTitle;
    TextView releaseYear;
    TextView ratings;
    ConstraintLayout search_cardView;

    TvPosterViewHolder(View itemView) {
        super(itemView);
        posterImage = itemView.findViewById(R.id.searched_movie_poster);
        tvShowTitle = itemView.findViewById(R.id.searched_movie_name);
        releaseYear = itemView.findViewById(R.id.searched_release_year);
        ratings = itemView.findViewById(R.id.searched_movie_rating);
        search_cardView = itemView.findViewById(R.id.search_cardView);
    }
    void bindView(TvShow tvShow)
    {
        if(tvShow != null) {
            if(tvShow.getPoster_path() == null){
                Picasso.get().load(R.drawable.not_found).into(posterImage);
            }else {
                Picasso.get().load("https://image.tmdb.org/t/p/w500" + tvShow.getPoster_path()).into(posterImage);
            }
            tvShowTitle.setText(tvShow.getName());
            if (tvShow.getFirst_air_date() != null && !tvShow.getFirst_air_date().equals(""))
                releaseYear.setText("Release: " + tvShow.getFirst_air_date().substring(0, 4));
            ratings.setText("Rating: " + tvShow.getVote_average());
        }
    }
}
}

