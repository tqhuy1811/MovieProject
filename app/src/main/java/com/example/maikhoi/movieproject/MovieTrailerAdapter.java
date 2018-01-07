package com.example.maikhoi.movieproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by MaiKhoi on 1/5/18.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.TrailerHolder> {
    private MovieDataTrailers[] trailers;
    private Context context;
    private final MovieTrailerAdapterOnCickHandler onCickHandler;

    public MovieTrailerAdapter(Context context, MovieTrailerAdapterOnCickHandler onCickHandler){
        this.context = context;
        this.onCickHandler = onCickHandler;

    }

    public interface MovieTrailerAdapterOnCickHandler{
        void onClick(MovieDataTrailers movieDataTrailers);
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.trailer_display_layout,parent,false);
        TrailerHolder holder = new TrailerHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        holder.trailerName.setText(trailers[position].name);

    }

    @Override
    public int getItemCount() {
        if (null == trailers) return 0;
        return trailers.length;
    }

    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageButton playButton;
        public TextView trailerName;
        public TrailerHolder(View itemView) {
            super(itemView);
            playButton = itemView.findViewById(R.id.play_trailer);
            trailerName = itemView.findViewById(R.id.movie_trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieDataTrailers data = trailers[adapterPosition];
            onCickHandler.onClick(data);
        }
    }
    public void setTrailersData(MovieDataTrailers[] movieDataTrailers){
        trailers = movieDataTrailers;
        notifyDataSetChanged();


    }
}
