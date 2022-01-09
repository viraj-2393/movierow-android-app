package com.movies.movierow.Utils;

import android.app.Application;

import com.movies.movierow.Models.MovieID;

public class MrProjectApp extends Application {
    static MovieID movieID;

    public static void setMovieID(MovieID mID){
        movieID = mID;
    }
    public static MovieID getMovieID(){
        if(movieID == null){
            movieID = new MovieID();
        }
        return movieID;
    }
}
