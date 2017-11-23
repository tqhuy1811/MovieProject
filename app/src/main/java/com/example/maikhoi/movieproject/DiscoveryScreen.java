package com.example.maikhoi.movieproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maikhoi.movieproject.JSONHelper.MovieDBJSONHelper;
import com.example.maikhoi.movieproject.MovieDBHelper.MovieDBAPI_Wrapper;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class DiscoveryScreen extends AppCompatActivity {
  private RecyclerView recyclerView;
  private GridLayoutManager layoutManager;
  private MoviePosterAdapter movieAdapter;
  private  String choice;
  private final static   String apiPopular= "http://api.themoviedb.org/3/movie/popular?api_key=98ff9a55378b9f07827a7dc0925d7d77";
  private final static  String apiTopRated = "http://api.themoviedb.org/3/movie/top_rated?api_key=98ff9a55378b9f07827a7dc0925d7d77";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_screen);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(DiscoveryScreen.this,3);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        movieAdapter = new MoviePosterAdapter(DiscoveryScreen.this);
        recyclerView.setAdapter(movieAdapter);
        choice = apiPopular;
        printData(choice);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortedmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.popular_sorted:
                choice=apiPopular;
                printData(choice);
                return true;
            case R.id.top_rated:
                choice=apiTopRated;
                printData(choice);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void printData(String api) {
        new testing().execute(api);
    }

    public class testing extends AsyncTask<String,Void,String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            String url = strings[0];

            URL getURL = MovieDBAPI_Wrapper.buildURL(url);
            try {
                String json = MovieDBAPI_Wrapper.getDataFromAPI(getURL);

                String[] array = MovieDBJSONHelper.getDataFromMovieDB(DiscoveryScreen.this,json);
                return array;
            }catch (Exception e) {
            e.printStackTrace();
            return  null;
            }

        }

        @Override
        protected void onPostExecute(String[] strings) {
            if(strings != null) {
                movieAdapter.setImageData(strings);
            }
        }
    }
    }



