package com.example.maikhoi.movieproject.MovieDBHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.maikhoi.movieproject.MovieData;

/**
 * Created by MaiKhoi on 1/6/18.
 */

public class MovieContentProvider extends ContentProvider {
    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_ID = 101;
    private MovieDBHelper movieDBHelper;
    private static final UriMatcher uriMatcher = buildUriMatcher();
    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieDataEntry.AUTHORITY,MovieDataEntry.PATH_DATA, MOVIE);
        uriMatcher.addURI(MovieDataEntry.AUTHORITY,MovieDataEntry.PATH_DATA+"/#", MOVIE_WITH_ID);
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        movieDBHelper = new MovieDBHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = movieDBHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        Cursor returnCursor;
        switch (match){
            case MOVIE:
                returnCursor = db.query(MovieDataEntry.MovieEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);

                break;
            default:
                throw new UnsupportedOperationException();
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return  returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case MOVIE:
                long id = db.insert(MovieDataEntry.MovieEntry.TABLE_NAME,null,contentValues);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(MovieDataEntry.MovieEntry.CONTENT_URI,id);
                }
                else{
                    throw new android.database.SQLException();
                }
                break;
            default:
                throw new UnsupportedOperationException();
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
