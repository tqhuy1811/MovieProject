package com.example.maikhoi.movieproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maikhoi.movieproject.JSONHelper.MovieDBJSONHelper;
import com.example.maikhoi.movieproject.MovieDBHelper.MovieDBAPI_Wrapper;
import com.example.maikhoi.movieproject.MovieDBHelper.MovieDataEntry;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

    public class DetailViewActivity extends AppCompatActivity implements MovieTrailerAdapter.MovieTrailerAdapterOnCickHandler {
    private ImageView poster_display;
    private TextView release_date;
    private TextView plot;
    private TextView original_title;
    private TextView userRating;
    private String id;
    private MovieData data;

    private RecyclerView trailer;
    private LinearLayoutManager layoutManager;
    private MovieTrailerAdapter movieTrailerAdapter;
    private RecyclerView reviews;
    private LinearLayoutManager layoutManagerReviews;
    private MovieReviewsAdapter movieReviewsAdapter;
    private Button saveToDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        poster_display =  findViewById(R.id.movie_poster_detail);
        release_date = findViewById(R.id.date_release_display);

        plot = findViewById(R.id.plot_synopsis);
        original_title = findViewById(R.id.original_title);
        userRating = findViewById(R.id.user_rating);
        setTitle(getString(R.string.movie_detail_screen));
        if(getIntent()!=null){
        data = getIntent().getParcelableExtra(getString(R.string.movie_data_transfer_api));

        if(data!=null){
            Picasso.with(this).load("http://image.tmdb.org/t/p/w342/"+data.imageLink).into(poster_display);
            release_date.setText(data.releaseDate);
            id = data.id;
            plot.setText(data.plot);
            original_title.setText(data.originalTitle);
            userRating.setText(data.userRating);
        }
        }
        saveToDataBase = findViewById(R.id.favourite_button);
        saveToDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieDataEntry.MovieEntry.COLUMN_MOVIE_POSTER,data.imageLink);
                contentValues.put(MovieDataEntry.MovieEntry.COLUMN_MOVIE_PLOT,data.plot);
                contentValues.put(MovieDataEntry.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,data.releaseDate);
                contentValues.put(MovieDataEntry.MovieEntry.COLUMN_MOVIE_TITLE,data.originalTitle);
                contentValues.put(MovieDataEntry.MovieEntry.COLUMN_MOVIE_ID,data.id);
                contentValues.put(MovieDataEntry.MovieEntry.COLUMN_MOVIE_USER_RATING,data.userRating);
                Uri uri = getContentResolver().insert(MovieDataEntry.MovieEntry.CONTENT_URI , contentValues);
                if(uri!=null){
                    Toast.makeText(getBaseContext(),uri.toString(),Toast.LENGTH_LONG).show();
                }
                finish();

            }
        });
        trailer = findViewById(R.id.recycler_view_trailer);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        trailer.setLayoutManager(layoutManager);
        trailer.setHasFixedSize(true);
        movieTrailerAdapter = new MovieTrailerAdapter(this
        ,this);
        trailer.setAdapter(movieTrailerAdapter);
        new FetchTrailerData().execute(id);
        reviews = findViewById(R.id.recycler_view_review);
        layoutManagerReviews = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        reviews.setLayoutManager(layoutManagerReviews);
        reviews.setHasFixedSize(true);
        movieReviewsAdapter = new MovieReviewsAdapter();
        reviews.setAdapter(movieReviewsAdapter);
        new FetchReviewsData().execute(id);

    }

    @Override
    public void onClick(MovieDataTrailers movieDataTrailers) {
        String url = "https://www.youtube.com/watch";
        Uri webpage = Uri.parse(url).buildUpon().appendQueryParameter("v",movieDataTrailers.key).build();

        Intent intent = new Intent(Intent.ACTION_VIEW,webpage);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }


    }


    public class FetchTrailerData extends AsyncTask<String,Void,MovieDataTrailers[]>{

        @Override
        protected MovieDataTrailers[] doInBackground(String... urls) {
            if (urls.length==0) return null;
            String id = urls[0];
            URL getURL = MovieDBAPI_Wrapper.buildURLMovieTrailers(id);
            try{
                String json = MovieDBAPI_Wrapper.getDataFromAPI(getURL);
                MovieDataTrailers[] data = MovieDBJSONHelper.getYouTubeLinks(json);
                return data;

            }catch(Exception e){
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(MovieDataTrailers[] movieDataTrailers) {
            super.onPostExecute(movieDataTrailers);
            movieTrailerAdapter.setTrailersData(movieDataTrailers);
        }
    }
    public class FetchReviewsData extends AsyncTask<String,Void,MovieReviewsData[]>{

        @Override
        protected MovieReviewsData[] doInBackground(String... urls) {
            if (urls.length==0) return null;
            String id = urls[0];
            URL getURL = MovieDBAPI_Wrapper.buildURLMovieReviews(id);
            try{
                String json = MovieDBAPI_Wrapper.getDataFromAPI(getURL);
                MovieReviewsData[] data = MovieDBJSONHelper.getReviewsData(json);
                return data;

            }catch(Exception e){
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(MovieReviewsData[] movieReviewsData) {
            super.onPostExecute(movieReviewsData);
            movieReviewsAdapter.setReviewsData(movieReviewsData);
        }
    }



}
