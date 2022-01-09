package com.movies.movierow.Models;


import java.io.Serializable;
import java.util.List;

public class KidMoviesModel implements Serializable {
    public int page;
    public List<KidMovie> results;
    public int total_pages;
    public int total_results;

    public int getPage() {
        return page;
    }

    public List<KidMovie> getResults() {
        return results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }
}
