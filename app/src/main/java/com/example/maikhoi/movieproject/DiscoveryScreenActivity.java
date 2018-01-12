package com.example.maikhoi.movieproject;



import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import com.example.maikhoi.movieproject.JSONHelper.MovieDBJSONHelper;
import com.example.maikhoi.movieproject.MovieDBHelper.MovieDBAPI_Wrapper;
import com.example.maikhoi.movieproject.MovieDBHelper.MovieDataEntry;
import com.example.maikhoi.movieproject.MoviePosterAdapter.MoviePosterAdapterOnClickHandler;

import java.net.URL;

public class DiscoveryScreenActivity extends AppCompatActivity implements MoviePosterAdapterOnClickHandler,LoaderCallbacks<Cursor>,CustomCursorMovieAdapter.MovieDBOnClickHandler{
    private RecyclerView recyclerView;

    private GridLayoutManager layoutManager;

    private MoviePosterAdapter movieAdapter;
    private String choice;
    private LoaderCallbacks<MovieData[]> callbacks;

    private final static int LOADER_UNIQUE_ID = 18;
    private final static int LOADER_UNIQUE_ID_DATA = 20;
    private static String statedChecked;

    private final static String apiPopular = "http://api.themoviedb.org/3/movie/popular?";
    private final static String apiTopRated = "http://api.themoviedb.org/3/movie/top_rated?";
    private CustomCursorMovieAdapter customCursorMovieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_screen);
        setTitle(getString(R.string.most_popular));
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(DiscoveryScreenActivity.this, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        movieAdapter = new MoviePosterAdapter(DiscoveryScreenActivity.this, this);
        customCursorMovieAdapter = new CustomCursorMovieAdapter(this,this);
        recyclerView.setAdapter(movieAdapter);
        if(savedInstanceState!=null){
            choice = savedInstanceState.getString(getString(R.string.api_checked));
            statedChecked = savedInstanceState.getString(getString(R.string.menu_title));
            if(statedChecked == getString(R.string.most_popular)){

                recyclerView.setAdapter(movieAdapter);
                getSupportLoaderManager().restartLoader(LOADER_UNIQUE_ID_DATA,null,movieDataLoader());
                setTitle(statedChecked);
            }
            else if(statedChecked == getString(R.string.top_rated)){
                recyclerView.setAdapter(movieAdapter);
                getSupportLoaderManager().restartLoader(LOADER_UNIQUE_ID_DATA,null,movieDataLoader());
                setTitle(statedChecked);

            }
            else  if(statedChecked == getString(R.string.favourite_menu_sorted)){
                recyclerView.setAdapter(customCursorMovieAdapter);
                getSupportLoaderManager().restartLoader(LOADER_UNIQUE_ID_DATA,null,movieDataLoader());
                setTitle(statedChecked);
            }
        }

        choice = apiPopular;
        statedChecked = getTitle().toString();
        getSupportLoaderManager().initLoader(LOADER_UNIQUE_ID_DATA,null,movieDataLoader());
        getSupportLoaderManager().initLoader(LOADER_UNIQUE_ID,null,this);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        statedChecked = savedInstanceState.getString(getString(R.string.menu_title));
        choice = savedInstanceState.getString(getString(R.string.api_checked));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(getString(R.string.menu_title),statedChecked);
        outState.putString(getString(R.string.api_checked),choice);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortedmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_sorted:
               recyclerView.setAdapter(movieAdapter);
                choice = apiPopular;
                getSupportLoaderManager().restartLoader(LOADER_UNIQUE_ID_DATA,null,movieDataLoader());


                setTitle(getString(R.string.most_popular));
                statedChecked = getTitle().toString();
                return true;
            case R.id.top_rated:
                recyclerView.setAdapter(movieAdapter);
                choice = apiTopRated;

                getSupportLoaderManager().restartLoader(LOADER_UNIQUE_ID_DATA,null,movieDataLoader());
                setTitle(R.string.top_rated);
                statedChecked = getTitle().toString();
                return true;
            case R.id.favourite_list:
                setTitle(R.string.favourite_menu_sorted);
                recyclerView.setAdapter(customCursorMovieAdapter);

                getSupportLoaderManager().restartLoader(LOADER_UNIQUE_ID,null,this);
                statedChecked = getTitle().toString();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }




    public LoaderCallbacks<MovieData[]> movieDataLoader(){
        callbacks = new LoaderCallbacks<MovieData[]>() {
            @Override
            public android.support.v4.content.Loader<MovieData[]> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<MovieData[]>(getBaseContext()) {
                    MovieData[] cachedData = null;

                    @Override
                    protected void onStartLoading() {
                        if(cachedData != null){
                            deliverResult(cachedData);
                        }
                        else{
                            forceLoad();;
                        }
                    }

                    @Override
                    public MovieData[] loadInBackground() {
                        String url = choice;
                        URL getURL = MovieDBAPI_Wrapper.buildURL(url);
                        try{
                            String json = MovieDBAPI_Wrapper.getDataFromAPI(getURL);
                            MovieData[] data = MovieDBJSONHelper.getDataFromMovieDB(json);
                            return data;

                        }catch(Exception e){
                            e.printStackTrace();
                            return null;
                        }

                    }
                    public void deliverResult(MovieData[] data){
                        cachedData = data;
                        super.deliverResult(data);
                    }
                };
            }

            @Override
            public void onLoadFinished(android.support.v4.content.Loader<MovieData[]> loader, MovieData[] data) {
                        movieAdapter.setImageData(data);
            }

            @Override
            public void onLoaderReset(android.support.v4.content.Loader<MovieData[]> loader) {

            }
        };
        return  callbacks;
    }

    private boolean checkInternetConnection(){
        Context context = DiscoveryScreenActivity.this;
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;

    }


    @Override
    public void onClick(MovieData movieData) {
        Intent intent = new Intent(DiscoveryScreenActivity.this, DetailViewActivity.class);
        intent.putExtra(getString(R.string.movie_data_transfer_api), movieData);

        startActivity(intent);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor dataM = null;

            @Override
            protected void onStartLoading() {
                if(dataM != null){
                    deliverResult(dataM);
                }else{
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
               try{
                   return getContentResolver().query(MovieDataEntry.MovieEntry.CONTENT_URI,null,null,null, MovieDataEntry.MovieEntry.COLUMN_MOVIE_ID);

               }catch (Exception e){
                   e.printStackTrace();
                   return null;
               }
            }
            public void deliverResult(Cursor data){
                dataM = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        customCursorMovieAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
            customCursorMovieAdapter.swapCursor(null);
    }


    @Override
    public void onClickDB(Cursor cursor, int position) {

        int imageLink = cursor.getColumnIndex(MovieDataEntry.MovieEntry.COLUMN_MOVIE_POSTER);
        int id = cursor.getColumnIndex(MovieDataEntry.MovieEntry.COLUMN_MOVIE_ID);
        int releaseDate = cursor.getColumnIndex(MovieDataEntry.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
        int title = cursor.getColumnIndex(MovieDataEntry.MovieEntry.COLUMN_MOVIE_TITLE);
        int userRating = cursor.getColumnIndex(MovieDataEntry.MovieEntry.COLUMN_MOVIE_USER_RATING);
        int plot = cursor.getColumnIndex(MovieDataEntry.MovieEntry.COLUMN_MOVIE_PLOT);
        cursor.moveToPosition(position);
        String imageDB = cursor.getString(imageLink);
        String idDB = cursor.getString(id);
        String releaseDateDB = cursor.getString(releaseDate);
        String titleDB = cursor.getString(title);
        String userRatingDB = cursor.getString(userRating);
        String plotDB = cursor.getString(plot);

        MovieData movieData = new MovieData(imageDB,releaseDateDB,titleDB,plotDB,userRatingDB,idDB);
        Intent intent = new Intent(DiscoveryScreenActivity.this, DetailViewActivity.class);
        intent.putExtra(getString(R.string.movie_data_transfer_db),movieData);
        intent.putExtra(getString(R.string.button_hide),"true");
        startActivity(intent);

    }
}



