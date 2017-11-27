package com.example.maikhoi.movieproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.maikhoi.movieproject.JSONHelper.MovieDBJSONHelper;
import com.example.maikhoi.movieproject.MovieDBHelper.MovieDBAPI_Wrapper;

import java.net.URL;

/**
 * Created by MaiKhoi on 11/26/17.
 */

public class FetchData extends AsyncTask<String,Void,MovieData[]> {
    private Context context;
    private AsyncTaskCompleteListener listener;

    public FetchData(Context context, AsyncTaskCompleteListener listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected MovieData[] doInBackground(String... strings) {
        String url = strings[0];

        URL getURL = MovieDBAPI_Wrapper.buildURL(url,context);
        Log.i("INFO",getURL.toString());
        try {
            String json = MovieDBAPI_Wrapper.getDataFromAPI(getURL);

            MovieData[] array = MovieDBJSONHelper.getDataFromMovieDB(json);
            return array;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(MovieData[] movieData) {
        super.onPostExecute(movieData);
        listener.onTaskComplete(movieData);

    }

}
