package com.movies.movierow.Models;

import java.util.ArrayList;
import java.util.List;

public class MovieID{
    public String status;
    public List<Integer> movieIds;

    public String getStatus() {
        return status;
    }

    public List<Integer> getMovieIds() {
        if(movieIds == null){
            movieIds = new ArrayList<>();
            movieIds.add(00000);
        }
        return movieIds;
    }
}
