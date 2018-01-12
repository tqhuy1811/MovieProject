package com.example.maikhoi.movieproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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
    private  String ID;
    private MovieData data;
    private MovieData dataFromDB;
    private RecyclerView trailer;
    private LinearLayoutManager layoutManager;
    private MovieTrailerAdapter movieTrailerAdapter;
    private RecyclerView reviews;
    private  LinearLayoutManager layoutManagerReviews;
    private MovieReviewsAdapter movieReviewsAdapter;
    private Button saveToDataBase;
    private  LoaderManager.LoaderCallbacks<MovieDataTrailers[]> callbacksTrailers;
    private  LoaderManager.LoaderCallbacks<MovieReviewsData[]> callbacksReviews;
    private final static int LOADER_UNIQUE_ID_TRAILERS = 18;
    private final static int LOADER_UNIQUE_ID_REVIEWS = 20;
    private static boolean checkSavedData;
    private static boolean hideButton = false;
    private static Cursor cursorChecked;



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
        dataFromDB = getIntent().getParcelableExtra(getString(R.string.movie_data_transfer_db));
        hideButton = Boolean.valueOf(getIntent().getStringExtra(getString(R.string.button_hide)));
        if(data!=null){
            Picasso.with(this).load(getString(R.string.image_link)+data.imageLink).into(poster_display);
            release_date.setText(data.releaseDate);
            ID = data.id;
            plot.setText(data.plot);
            original_title.setText(data.originalTitle);
            userRating.setText(data.userRating);
        }
        if(dataFromDB != null){
            Picasso.with(this).load(getString(R.string.image_link)+dataFromDB.imageLink).into(poster_display);
            release_date.setText(dataFromDB.releaseDate);
            ID = dataFromDB.id;
            plot.setText(dataFromDB.plot);
            original_title.setText(dataFromDB.originalTitle);
            userRating.setText(dataFromDB.userRating);
        }
        }
        saveToDataBase = findViewById(R.id.favourite_button);
        if(hideButton){
            saveToDataBase.setVisibility(View.INVISIBLE);
        }
        checkedForMovieDB();
//        if (cursorChecked != null){
//            saveToDataBase.setText(getString(R.string.favourite_button_second_stage));
//        }
        saveToDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(cursorChecked == null){

                checkSavedData = saveToDataBase.isPressed();
                saveDataToDB();
            }
            else{

                Toast.makeText(DetailViewActivity.this,"You already saved this movie",Toast.LENGTH_SHORT).show();
            }

//
            }
        });
        if(savedInstanceState != null){
            checkSavedData = savedInstanceState.getBoolean(getString(R.string.boolean_check));
            if(checkSavedData==true){
                saveToDataBase.setText(getString(R.string.favourite_button_second_stage));
            }


        }

        trailer = findViewById(R.id.recycler_view_trailer);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        trailer.setLayoutManager(layoutManager);
        trailer.setHasFixedSize(true);
        movieTrailerAdapter = new MovieTrailerAdapter(this
        ,this);
        trailer.setAdapter(movieTrailerAdapter);
        loaderDataTrailers();
        getSupportLoaderManager().initLoader(LOADER_UNIQUE_ID_TRAILERS,null,callbacksTrailers);
        reviews = findViewById(R.id.recycler_view_review);
        layoutManagerReviews = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        reviews.setLayoutManager(layoutManagerReviews);
        reviews.setHasFixedSize(true);
        movieReviewsAdapter = new MovieReviewsAdapter();
        reviews.setAdapter(movieReviewsAdapter);
        loaderDataReviews();
        getSupportLoaderManager().initLoader(LOADER_UNIQUE_ID_REVIEWS,null,callbacksReviews);

    }

    @Override
    public void onClick(MovieDataTrailers movieDataTrailers) {
        String url = getString(R.string.url_youtube);
        Uri webpage = Uri.parse(url).buildUpon().appendQueryParameter("v",movieDataTrailers.key).build();

        Intent intent = new Intent(Intent.ACTION_VIEW,webpage);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }


    }

        @Override
        protected void onRestoreInstanceState(Bundle savedInstanceState) {
            checkSavedData = savedInstanceState.getBoolean(getString(R.string.boolean_check));
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            outState.putBoolean(getString(R.string.boolean_check),checkSavedData);
            super.onSaveInstanceState(outState);
        }

        public void saveDataToDB(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDataEntry.MovieEntry.COLUMN_MOVIE_POSTER,data.imageLink);
        contentValues.put(MovieDataEntry.MovieEntry.COLUMN_MOVIE_PLOT,data.plot);
        contentValues.put(MovieDataEntry.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,data.releaseDate);
        contentValues.put(MovieDataEntry.MovieEntry.COLUMN_MOVIE_TITLE,data.originalTitle);
        contentValues.put(MovieDataEntry.MovieEntry.COLUMN_MOVIE_ID,data.id);
        contentValues.put(MovieDataEntry.MovieEntry.COLUMN_MOVIE_USER_RATING,data.userRating);
        Uri uri = getContentResolver().insert(MovieDataEntry.MovieEntry.CONTENT_URI , contentValues);
    }


    public Cursor checkedForMovieDB(){
            if(data!=null){
            String id  = data.id.toString();
            Uri uri = MovieDataEntry.MovieEntry.CONTENT_URI;
            uri.buildUpon().appendPath(id).build();
           cursorChecked = getContentResolver().query(uri,null,null,null,null);
            }
        return  cursorChecked;
    }

    private  LoaderManager.LoaderCallbacks<MovieDataTrailers[]> loaderDataTrailers(){
        callbacksTrailers = new LoaderManager.LoaderCallbacks<MovieDataTrailers[]>() {
            @Override
            public Loader<MovieDataTrailers[]> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<MovieDataTrailers[]>(getBaseContext()) {
                    MovieDataTrailers[] cachedData = null;

                    @Override
                    protected void onStartLoading() {
                        if(cachedData != null){
                            deliverResult(cachedData);
                        }
                        else{
                            forceLoad();
                        }
                    }

                    @Override
                    public MovieDataTrailers[] loadInBackground() {
                        URL getURL = MovieDBAPI_Wrapper.buildURLMovieTrailers(ID);
                        try{
                            String json = MovieDBAPI_Wrapper.getDataFromAPI(getURL);
                            MovieDataTrailers[] data = MovieDBJSONHelper.getYouTubeLinks(json);
                            return data;

                        }catch(Exception e){
                            e.printStackTrace();
                            return null;
                        }
                    }
                    public void deliverResult(MovieDataTrailers[] dataTrailers){
                        cachedData = dataTrailers;
                        super.deliverResult(dataTrailers);
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<MovieDataTrailers[]> loader, MovieDataTrailers[] data) {
                movieTrailerAdapter.setTrailersData(data);

            }

            @Override
            public void onLoaderReset(Loader<MovieDataTrailers[]> loader) {

            }
        };
        return callbacksTrailers;
        }

    private LoaderManager.LoaderCallbacks<MovieReviewsData[]> loaderDataReviews(){
        callbacksReviews = new LoaderManager.LoaderCallbacks<MovieReviewsData[]>() {
            @Override
            public Loader<MovieReviewsData[]> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<MovieReviewsData[]>(getBaseContext()) {
                    MovieReviewsData[] cacheData = null;

                    @Override
                    protected void onStartLoading() {
                        if(cacheData!=null){
                            deliverResult(cacheData);
                        }
                        else{
                            forceLoad();;
                        }
                    }

                    @Override
                    public MovieReviewsData[] loadInBackground() {
                        URL getURL = MovieDBAPI_Wrapper.buildURLMovieReviews(ID);
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
                    public void deliverResult(MovieReviewsData[] data) {
                        cacheData = data;
                        super.deliverResult(data);
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<MovieReviewsData[]> loader, MovieReviewsData[] data) {
                movieReviewsAdapter.setReviewsData(data);

            }

            @Override
            public void onLoaderReset(Loader<MovieReviewsData[]> loader) {

            }
        };
        return callbacksReviews;
    }

    }




