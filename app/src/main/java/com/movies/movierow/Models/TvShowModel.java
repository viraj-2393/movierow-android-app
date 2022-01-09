package com.movies.movierow.Models;


import java.io.Serializable;
import java.util.List;

public class TvShowModel implements Serializable {
    public int page;
    public List<TvShow> results;
    public int total_pages;
    public int total_results;

    public int getPage() {
        return page;
    }

    public List<TvShow> getResults() {
        return results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }
}


