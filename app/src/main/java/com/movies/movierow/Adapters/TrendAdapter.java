package com.movies.movierow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.movies.movierow.Models.Cast;
import com.movies.movierow.Models.CastDetails;
import com.movies.movierow.Models.TrendingActors;
import com.movies.movierow.Models.Trends;
import com.movies.movierow.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrendAdapter extends RecyclerView.Adapter {
    private final Activity activity;
    List<TrendingActors> actors;
    private Context ctx;
    // private OnFeedItemClickListener onFeedItemClickListener;

    public TrendAdapter(Trends trends, Context ctx, Activity activity) {
        this.actors = trends.getResults();
        this.ctx = ctx;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.trends_cardview,parent,false);
        TrendAdapter.TrendPosterViewHolder TrendPosterViewHolder=new TrendAdapter.TrendPosterViewHolder(view);
        return TrendPosterViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TrendAdapter.TrendPosterViewHolder) holder).bindView(actors.get(position));
    }


    @Override
    public int getItemCount() {
        return actors.size();
    }

    public interface OnFeedItemClickListener {
        void onEditOffer(View v, int position);
    }

    public class TrendPosterViewHolder extends RecyclerView.ViewHolder
    {

        ImageView posterImage;
        TextView originalName;
        TextView dept_name;

        TrendPosterViewHolder(View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.actor_poster);
            originalName = itemView.findViewById(R.id.actor_name);
            dept_name = itemView.findViewById(R.id.dept_name);

        }
        void bindView(TrendingActors trendingActors)
        {
            if(trendingActors != null) {
                if(trendingActors.getProfilePath() == null){
                    Picasso.get().load(R.drawable.not_found).into(posterImage);
                }else {
                    Picasso.get().load("https://image.tmdb.org/t/p/w500" + trendingActors.getProfilePath()).into(posterImage);
                }
                originalName.setText(trendingActors.getName());
                dept_name.setText(trendingActors.getKnownForDepartment());
            }
        }
    }
}
