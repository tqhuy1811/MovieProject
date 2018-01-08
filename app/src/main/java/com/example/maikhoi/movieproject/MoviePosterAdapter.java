package com.example.maikhoi.movieproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by MaiKhoi on 11/22/17.
 */

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.ImageHolder> {
    private MovieData[] movieData;
    private Context context;
    private final MoviePosterAdapterOnClickHandler clickHandler;

    public MoviePosterAdapter(Context context, MoviePosterAdapterOnClickHandler handler){
        clickHandler = handler;
        this.context = context;
    }

    public interface MoviePosterAdapterOnClickHandler{
        void onClick(MovieData  movieData);
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.poster_display_layout, parent, false);
        ImageHolder holder = new ImageHolder(layout);

        return holder;
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        Picasso.with(context).load(context.getString(R.string.image_link)+movieData[position].imageLink).into(holder.movie_poster);
    }

    @Override
    public int getItemCount() {
        if(null == movieData) return 0;
        return movieData.length;

    }
    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView movie_poster;
        public ImageHolder(View itemView) {
            super(itemView);
            movie_poster = itemView.findViewById(R.id.poster_display_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adpaterPosition = getAdapterPosition();
            MovieData data = movieData[adpaterPosition];
            clickHandler.onClick(data);
        }
    }

    public void setImageData(MovieData[] data){
        movieData = data;
        notifyDataSetChanged();
    }
}
