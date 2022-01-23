package com.movies.movierow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.movies.movierow.Activities.AboutMovie;
import com.movies.movierow.Models.FavModal;
import com.movies.movierow.Models.TrendingActors;
import com.movies.movierow.Models.Trends;
import com.movies.movierow.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class YourFavAdapter extends RecyclerView.Adapter {
    private final Activity activity;
    List<FavModal> favModals;
    private Context ctx;
    // private OnFeedItemClickListener onFeedItemClickListener;

    public YourFavAdapter(List<FavModal> favModals, Context ctx, Activity activity) {
        this.favModals = favModals;
        this.ctx = ctx;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.trends_cardview,parent,false);
        YourFavAdapter.YourFavPosterViewHolder YourFavPosterViewHolder=new YourFavAdapter.YourFavPosterViewHolder(view);
        return YourFavPosterViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((YourFavAdapter.YourFavPosterViewHolder) holder).bindView(favModals.get(position));
        ((YourFavAdapter.YourFavPosterViewHolder) holder).posterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AboutMovie.class);
                intent.putExtra("position",position);
                intent.putExtra("obj",favModals.get(position));
                intent.putExtra("Caller","FAV");
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return favModals.size();
    }

    public interface OnFeedItemClickListener {
        void onEditOffer(View v, int position);
    }

    public class YourFavPosterViewHolder extends RecyclerView.ViewHolder
    {

        ImageView posterImage;
        TextView originalName;
        TextView dept_name;

        YourFavPosterViewHolder(View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.actor_poster);
            originalName = itemView.findViewById(R.id.actor_name);
            dept_name = itemView.findViewById(R.id.dept_name);

        }
        void bindView(FavModal favModal)
        {
            if(favModal != null) {
                if(favModal.getPoster_path() == null){
                    Picasso.get().load(R.drawable.not_found).into(posterImage);
                }else {

                    Picasso.get().load("https://image.tmdb.org/t/p/w500" + favModal.getPoster_path()).into(posterImage);
                }
                originalName.setText(favModal.getName());
                dept_name.setText(favModal.getYear());
            }
        }
    }
}
