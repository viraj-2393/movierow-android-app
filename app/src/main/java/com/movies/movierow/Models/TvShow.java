package com.movies.movierow.Models;

import java.io.Serializable;
import java.util.List;

public class TvShow implements Serializable {
    public String backdrop_path;
    public String first_air_date;
    public List<Integer> genre_ids;
    public int id;
    public String name;
    public List<String> origin_country;
    public String original_language;
    public String original_name;
    public String overview;
    public double popularity;
    public String poster_path;
    public double vote_average;
    public int vote_count;

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getOrigin_country() {
        return origin_country;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }
}
