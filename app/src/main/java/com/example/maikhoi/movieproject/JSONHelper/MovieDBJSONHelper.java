package com.example.maikhoi.movieproject.JSONHelper;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
/**
 * Created by MaiKhoi on 11/16/17.
 */

public class MovieDBJSONHelper {

    public static String[] getDataFromMovieDB(Context context, String movieJson) throws JSONException{

        final String Movie_Data = "results";
        final String Release_Date = "release_date";
        final String Movie_Vote_Average = "vote_average";
        final String Movie_Poster = "poster_path";
        final String Movei_Overview = "overview";
        String [] parsedMovieData = null;
        final String errorChecking = "total_results";
        JSONObject MovieJSONObject = new JSONObject(movieJson);
        JSONArray movieArray = MovieJSONObject.getJSONArray(Movie_Data);
        parsedMovieData = new String[movieArray.length()];
        for(int i=0;i<movieArray.length();i++){
            double vote_average;
            String over_view;
            String release_date;
            String poster_image;
            JSONObject display_movie_data = movieArray.getJSONObject(i);
            vote_average = display_movie_data.getDouble(Movie_Vote_Average);
            release_date = display_movie_data.getString(Release_Date);
            over_view = display_movie_data.getString(Movei_Overview);
            poster_image = display_movie_data.getString(Movie_Poster);

            parsedMovieData[i] = poster_image;
//                    + " - " + vote_average + " - " +  release_date + " - " + over_view;


        }
        return  parsedMovieData;
    }


}
