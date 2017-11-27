package com.example.maikhoi.movieproject;

/**
 * Created by MaiKhoi on 11/26/17.
 */

public interface AsyncTaskCompleteListener<T> {
    public void onTaskComplete(MovieData[] result);
}
