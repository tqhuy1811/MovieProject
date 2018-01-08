package com.example.maikhoi.movieproject;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.maikhoi.movieproject.MovieDBHelper.MovieDataEntry;
import com.squareup.picasso.Picasso;

/**
 * Created by MaiKhoi on 1/6/18.
 */

public class CustomCursorMovieAdapter extends RecyclerView.Adapter<CustomCursorMovieAdapter.CursorMovieDataHolder> {
    private Cursor mCursor;
    private Context mContext;
    private final MovieDBOnClickHandler movieDBOnClickHandler;
    public CustomCursorMovieAdapter(Context mContext, MovieDBOnClickHandler movieDBOnClickHandler){
        this.mContext = mContext;
        this.movieDBOnClickHandler = movieDBOnClickHandler;
    }


    public interface MovieDBOnClickHandler {
        void onClickDB(Cursor cursor,int position);
    }

    @Override
    public CursorMovieDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.poster_display_layout,parent,false);
        return new CursorMovieDataHolder(view);
    }

    @Override
    public void onBindViewHolder(CursorMovieDataHolder holder, int position) {

        int imageLink = mCursor.getColumnIndex(MovieDataEntry.MovieEntry.COLUMN_MOVIE_POSTER);
        mCursor.moveToPosition(position);

        String link = mCursor.getString(imageLink);


        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w780/"+link).into(holder.poster);

    }

    @Override
    public int getItemCount() {
        if(mCursor == null) return 0;
        return mCursor.getCount();
    }
    public Cursor swapCursor(Cursor c){
        if(mCursor == c){
            return  null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;
        if(c != null){
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class CursorMovieDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView poster;
        public CursorMovieDataHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster_display_image);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            movieDBOnClickHandler.onClickDB(mCursor,adapterPosition);


        }
    }
}
