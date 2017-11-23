package com.example.maikhoi.movieproject.MovieDBHelper;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by MaiKhoi on 11/16/17.
 */

public class MovieDBAPI_Wrapper {
//
//    private final String GET_MOVIE_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=";
//    private final String GET_MOVIE_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?api_key=";

    public static URL buildURL(String movieData){
        URL url = null;
        try{
            url = new URL(movieData);

        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;

    }
    public static  String getDataFromAPI(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }
            else{
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
