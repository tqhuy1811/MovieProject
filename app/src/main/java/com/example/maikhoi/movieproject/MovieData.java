package com.example.maikhoi.movieproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MaiKhoi on 11/25/17.
 */

public class MovieData implements Parcelable {
    String imageLink;
    String releaseDate;
    String original_title;
    String plot;
    String userRating;
    public MovieData(String imageLink,String releaseDate,String original_title,String plot,String userRating){
        this.imageLink = imageLink;
        this.releaseDate = releaseDate;
        this.original_title = original_title;
        this.plot = plot;
        this.userRating = userRating;
    }
    private MovieData(Parcel in){
        imageLink = in.readString();
        releaseDate = in.readString();
        original_title = in.readString();
        plot = in.readString();
        userRating = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(imageLink);
            parcel.writeString(releaseDate);
            parcel.writeString(original_title);
            parcel.writeString(plot);
            parcel.writeString(userRating);
    }
    public final static Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>(){

        @Override
        public MovieData createFromParcel(Parcel parcel) {
            return new MovieData(parcel);

        }

        @Override
        public MovieData[] newArray(int i) {
            return new MovieData[i];
        }
    };
}