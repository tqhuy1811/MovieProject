package com.example.maikhoi.movieproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by MaiKhoi on 1/5/18.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ReviewsHolder> {
    private MovieReviewsData[] movieReviewsData;

    @Override
    public ReviewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.reviews_display_layout,parent,false);
        ReviewsHolder holder = new ReviewsHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReviewsHolder holder, int position) {
        holder.authorName.setText(movieReviewsData[position].author);
        holder.contentReview.setText(movieReviewsData[position].content);

    }

    @Override
    public int getItemCount() {
        if(null == movieReviewsData) return 0;
        return movieReviewsData.length;
    }


    public class ReviewsHolder extends RecyclerView.ViewHolder{
        private TextView authorName;
        private TextView contentReview;

        public ReviewsHolder(View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.author_reviews);
            contentReview = itemView.findViewById(R.id.content_reviews);

        }
    }
    public void setReviewsData(MovieReviewsData[] movieReviewsData){
        this.movieReviewsData = movieReviewsData;
        notifyDataSetChanged();
    }

}
