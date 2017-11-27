package com.example.maikhoi.movieproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailView extends AppCompatActivity {
    private ImageView poster_display;
    private TextView release_date;
    private TextView plot;
    private TextView original_title;
    private TextView userRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        poster_display =  findViewById(R.id.movie_poster_detail);
        release_date = findViewById(R.id.date_release_display);

        plot = findViewById(R.id.plot_synopsis);
        original_title = findViewById(R.id.original_title);
        userRating = findViewById(R.id.user_rating);
        setTitle("Movie Detail");
        if(getIntent()!=null){
        MovieData data = getIntent().getParcelableExtra("DATA");
        if(data!=null){
            Picasso.with(this).load("http://image.tmdb.org/t/p/w342/"+data.imageLink).into(poster_display);
            release_date.setText(data.releaseDate);

            plot.setText(data.plot);
            original_title.setText(data.original_title);
            userRating.setText(data.userRating);
        }
        }
    }
}
