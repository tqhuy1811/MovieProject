package com.example.maikhoi.movieproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.maikhoi.movieproject.MoviePosterAdapter.MoviePosterAdapterOnClickHandler;
import com.example.maikhoi.movieproject.JSONHelper.MovieDBJSONHelper;
import com.example.maikhoi.movieproject.MovieDBHelper.MovieDBAPI_Wrapper;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class DiscoveryScreen extends AppCompatActivity implements MoviePosterAdapterOnClickHandler {
  private RecyclerView recyclerView;
  private GridLayoutManager layoutManager;
  private MoviePosterAdapter movieAdapter;
  private  String choice;
  //API KEY IS IN THE INFO KEY FILE
  private final static   String apiPopular= "http://api.themoviedb.org/3/movie/popular?api_key=";
  private final static  String apiTopRated = "http://api.themoviedb.org/3/movie/top_rated?api_key=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_screen);
        setTitle("Pop Movie");
        recyclerView =  findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(DiscoveryScreen.this,2);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        movieAdapter = new MoviePosterAdapter(DiscoveryScreen.this, this);
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
                setTitle("Pop Movie");
                return true;
            case R.id.top_rated:
                choice=apiTopRated;
                printData(choice);
                setTitle("Top Rated");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    private void printData(String api) {
        new testing().execute(api);
    }

    @Override
    public void onClick(MovieData movieData) {
        Intent intent = new Intent(DiscoveryScreen.this,DetailView.class);
        intent.putExtra("DATA", movieData);

        startActivity(intent);
    }

    public class testing extends AsyncTask<String,Void,MovieData[]> {

        @Override
        protected MovieData[] doInBackground(String... strings) {
            String url = strings[0];

            URL getURL = MovieDBAPI_Wrapper.buildURL(url);
            try {
                String json = MovieDBAPI_Wrapper.getDataFromAPI(getURL);

                MovieData[] array = MovieDBJSONHelper.getDataFromMovieDB(DiscoveryScreen.this,json);
                return array;
            }catch (Exception e) {
            e.printStackTrace();
            return  null;
            }

        }

        @Override
        protected void onPostExecute(MovieData[] strings) {
            if(strings != null) {
                movieAdapter.setImageData(strings);
            }
        }
    }
    }



