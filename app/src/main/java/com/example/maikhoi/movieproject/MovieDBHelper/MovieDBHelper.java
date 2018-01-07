package com.example.maikhoi.movieproject.MovieDBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MaiKhoi on 1/6/18.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moviedata.db";
    private static final int DATABASE_VERSION = 1;
    public MovieDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieDataEntry.MovieEntry.TABLE_NAME + "("
                + MovieDataEntry.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MovieDataEntry.MovieEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, "
                + MovieDataEntry.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "
                + MovieDataEntry.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                + MovieDataEntry.MovieEntry.COLUMN_MOVIE_PLOT + " TEXT NOT NULL, "
                + MovieDataEntry.MovieEntry.COLUMN_MOVIE_RELEASE_DATE +" TEXT NOT NULL, "
                + MovieDataEntry.MovieEntry.COLUMN_MOVIE_USER_RATING + " REAL NOT NULL "
                + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MovieDataEntry.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
