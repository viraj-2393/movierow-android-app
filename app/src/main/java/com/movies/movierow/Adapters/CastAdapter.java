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
import com.movies.movierow.Models.Cast;
import com.movies.movierow.Models.CastDetails;
import com.movies.movierow.Models.MovieDetails;
import com.movies.movierow.Models.PopularMoviesModel;
import com.movies.movierow.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter {
    private final Activity activity;
    CastDetails castDetails;
    private List<Cast> listdata;
    private Context ctx;
    // private OnFeedItemClickListener onFeedItemClickListener;

    public CastAdapter(CastDetails castDetails, Context ctx, Activity activity) {
        this.castDetails = castDetails;
        this.listdata = castDetails.getCast();
        this.ctx = ctx;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.cast_cardview,parent,false);
        CastAdapter.CastPosterViewHolder CastPosterViewHolder=new CastAdapter.CastPosterViewHolder(view);
        return CastPosterViewHolder;
    }

//    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
//        this.onFeedItemClickListener = onFeedItemClickListener;
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CastAdapter.CastPosterViewHolder) holder).bindView(listdata.get(position));
//        ((SearchAdapter.MoviePosterViewHolder) holder).search_cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), AboutMovie.class);
//                intent.putExtra("position", position);
//                intent.putExtra("obj", movies);
//                intent.putExtra("Caller", "101");
//                view.getContext().startActivity(intent);
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public interface OnFeedItemClickListener {
        void onEditOffer(View v, int position);
    }

    public class CastPosterViewHolder extends RecyclerView.ViewHolder
    {

        ImageView posterImage;
        TextView originalName;
        TextView character_name;

        CastPosterViewHolder(View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.actor_poster);
            originalName = itemView.findViewById(R.id.actor_name);
            character_name = itemView.findViewById(R.id.character_name);

        }
        void bindView(Cast cast)
        {
            if(cast != null) {
                if(cast.getProfilePath() == null){
                    Picasso.get().load(R.drawable.not_found).into(posterImage);
                }else {
                    Picasso.get().load("https://image.tmdb.org/t/p/original" + cast.getProfilePath()).into(posterImage);
                }
                originalName.setText(cast.getOriginalName());
                character_name.setText(cast.getCharacter());
            }
        }
    }
}

