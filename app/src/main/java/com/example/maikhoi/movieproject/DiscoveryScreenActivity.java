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

public class DiscoveryScreenActivity extends AppCompatActivity implements MoviePosterAdapterOnClickHandler,LoaderCallbacks<Cursor>{
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewCursor;
    private GridLayoutManager layoutManager;
    private GridLayoutManager layoutManagerCursor;
    private MoviePosterAdapter movieAdapter;
    private String choice;
    private boolean internetState;
    private final static int LOADER_UNIQUE_ID = 18;


    private final static String apiPopular = "http://api.themoviedb.org/3/movie/popular?";
    private final static String apiTopRated = "http://api.themoviedb.org/3/movie/top_rated?";
    private CustomCursorMovieAdapter customCursorMovieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_screen);
        setTitle("Pop Movie");
        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewCursor = findViewById(R.id.recycler_view_cursor);

        layoutManager = new GridLayoutManager(DiscoveryScreenActivity.this, 2);
        layoutManagerCursor = new GridLayoutManager(DiscoveryScreenActivity.this,2);

        internetState =checkInternetConnection();



        recyclerView.setHasFixedSize(true);
        recyclerViewCursor.setHasFixedSize(true);
        recyclerViewCursor.setLayoutManager(layoutManagerCursor);

        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MoviePosterAdapter(DiscoveryScreenActivity.this, this);
        customCursorMovieAdapter = new CustomCursorMovieAdapter(this);
        recyclerViewCursor.setAdapter(customCursorMovieAdapter);
        recyclerView.setAdapter(movieAdapter);

        choice = apiPopular;
        returnData(choice);

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
               
                choice = apiPopular;
               returnData(choice);

                setTitle("Pop Movie");
                return true;
            case R.id.top_rated:

                choice = apiTopRated;
                returnData(choice);

                setTitle("Top Rated");
                return true;
            case R.id.favourite_list:
                setTitle("Favourite");

                getSupportLoaderManager().initLoader(LOADER_UNIQUE_ID,null,this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
    public void returnData(String url){
        new FetchData().execute(url);
    }

    @Override
    public void onClick(MovieData movieData) {
        Intent intent = new Intent(DiscoveryScreenActivity.this, DetailViewActivity.class);
        intent.putExtra("DATA", movieData);

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

    public class FetchData extends AsyncTask<String,Void,MovieData[]> {

        @Override
        protected MovieData[] doInBackground(String... urls) {
            if (urls.length==0) return null;
            String url = urls[0];
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

        @Override
        protected void onPostExecute(MovieData[] data) {
            super.onPostExecute(data);
            movieAdapter.setImageData(data);
        }
    }


}



