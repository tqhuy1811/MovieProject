package com.example.maikhoi.movieproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maikhoi.movieproject.JSONHelper.MovieDBJSONHelper;
import com.example.maikhoi.movieproject.MovieDBHelper.MovieDBAPI_Wrapper;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class DiscoveryScreen extends AppCompatActivity {
    ImageView image;
    String json;
    String api = "http://api.themoviedb.org/3/movie/popular?api_key=";
    private TextView work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_screen);
        work = (TextView) findViewById(R.id.hahahahahaha);
        printData();
    }


    private void printData() {
        new testing().execute(api);
    }

    public class testing extends AsyncTask<String,Void,String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            String url = strings[0];

            URL getURL = MovieDBAPI_Wrapper.buildURL(url);
            try {
                String json = MovieDBAPI_Wrapper.getDataFromAPI(getURL);
                Log.i("info",json);
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

             for(String a: strings){
                work.append(a);}
            }

        }
    }
    }



