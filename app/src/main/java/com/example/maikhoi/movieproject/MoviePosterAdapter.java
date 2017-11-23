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
    private String[] imageLinks;
    private Context context;

    public MoviePosterAdapter(Context context){

        this.context = context;
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
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/"+imageLinks[position]).into(holder.movie_poster);
    }

    @Override
    public int getItemCount() {
        if(null == imageLinks) return 0;
        return imageLinks.length;

    }
    public static class ImageHolder extends RecyclerView.ViewHolder{
        public ImageView movie_poster;
        public ImageHolder(View itemView) {
            super(itemView);
            movie_poster = itemView.findViewById(R.id.poster_display_image);
        }
    }
    public void setImageData(String[] imageData){
        imageLinks = imageData;
    }
}
