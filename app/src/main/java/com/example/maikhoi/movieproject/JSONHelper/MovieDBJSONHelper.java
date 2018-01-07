package com.example.maikhoi.movieproject.JSONHelper;

import android.content.Context;
import android.util.Log;

import com.example.maikhoi.movieproject.MovieData;
import com.example.maikhoi.movieproject.MovieDataTrailers;
import com.example.maikhoi.movieproject.MovieReviewsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
/**
 * Created by MaiKhoi on 11/16/17.
 */

public class MovieDBJSONHelper {

    public static MovieData[] getDataFromMovieDB( String movieJson) throws JSONException{

        final String Movie_Data = "results";
        final String Release_Date = "release_date";
        final String Movie_Vote_Average = "vote_average";
        final String Movie_Poster = "poster_path";
        final String Movie_Overview = "overview";
        final String Movie_title = "original_title";
        final String Movie_Id = "id";
        MovieData[] parsedMovieData;
        JSONObject MovieJSONObject = new JSONObject(movieJson);
        JSONArray movieArray = MovieJSONObject.getJSONArray(Movie_Data);
        parsedMovieData = new MovieData[movieArray.length()];
        for(int i=0;i<movieArray.length();i++){
            String vote_average;
            String over_view;
            String release_date;
            String poster_image;
            String original_title;
            String id;
            JSONObject display_movie_data = movieArray.getJSONObject(i);
            vote_average = display_movie_data.getString(Movie_Vote_Average);
            release_date = display_movie_data.getString(Release_Date);
            over_view = display_movie_data.getString(Movie_Overview);
            poster_image = display_movie_data.getString(Movie_Poster);
            original_title = display_movie_data.getString(Movie_title);
            id  = display_movie_data.getString(Movie_Id);

            parsedMovieData[i] = new MovieData(poster_image,release_date,original_title,over_view,vote_average,id);


        }
        return  parsedMovieData;
    }
    public static MovieDataTrailers[] getYouTubeLinks(String json) throws JSONException{
        final String youtubeLink = "key";
        final String trailerName = "name";
        final String Movie_Data = "results";
        JSONObject trailerJsonObject = new JSONObject(json);
        JSONArray jsonArray = trailerJsonObject.getJSONArray(Movie_Data);
        MovieDataTrailers[] data = new MovieDataTrailers[jsonArray.length()];
        for(int i = 0; i<jsonArray.length();i++){
            String key;
            String name;
            JSONObject movieTrailer = jsonArray.getJSONObject(i);
            key = movieTrailer.getString(youtubeLink);
            name = movieTrailer.getString(trailerName);
            data[i] = new MovieDataTrailers(key,name);
        }
        return data;

    }
    public static MovieReviewsData[] getReviewsData(String json) throws JSONException{
        final String authorReviews = "author";
        final String contentReviews = "content";
        final String Movie_Data = "results";
        JSONObject reviewJsonObject = new JSONObject(json);
        JSONArray jsonArray = reviewJsonObject.getJSONArray(Movie_Data);
        MovieReviewsData[] data = new MovieReviewsData[jsonArray.length()];
        for(int i = 0; i<jsonArray.length();i++){
            String author;
            String content;
            JSONObject movieReviews = jsonArray.getJSONObject(i);
            author = movieReviews.getString(authorReviews);
            content = movieReviews.getString(contentReviews);
            data[i] = new MovieReviewsData(author,content);
        }
        return data;

    }


}
