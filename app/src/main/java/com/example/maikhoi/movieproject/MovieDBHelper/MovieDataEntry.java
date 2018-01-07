package com.example.maikhoi.movieproject.MovieDBHelper;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

/**
 * Created by MaiKhoi on 1/6/18.
 */

public class MovieDataEntry {

    public static final String AUTHORITY = "com.example.maikhoi.movieproject";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_DATA = "movie";

    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DATA).build();
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_POSTER = "imageLinks";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_MOVIE_PLOT = "plot";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_USER_RATING = "userRatings";
        public static final String COLUMN_MOVIE_TITLE = "originalTitle";

    }
}
