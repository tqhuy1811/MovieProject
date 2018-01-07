package com.example.maikhoi.movieproject.MovieDBHelper;

import android.content.Context;
import android.net.Uri;

import com.example.maikhoi.movieproject.BuildConfig;

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
    private final static String api = BuildConfig.API_KEY;

    private static final String movieApi = "https://api.themoviedb.org/3/movie";
    public static URL buildURL(String movieData){

        Uri builtUri = Uri.parse(movieData).buildUpon().appendQueryParameter("api_key",api).build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());

        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;

    }
    public static URL buildURLMovieTrailers(String id){
        Uri builtUri = Uri.parse(movieApi).buildUpon().appendPath(id).appendPath("videos").appendQueryParameter("api_key",api).build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());

        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return  url;
    }
    public static URL buildURLMovieReviews(String id){
        Uri builtUri = Uri.parse(movieApi).buildUpon().appendPath(id).appendPath("reviews").appendQueryParameter("api_key",api).build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());

        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return  url;
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
