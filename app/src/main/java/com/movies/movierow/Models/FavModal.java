package com.movies.movierow.Models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

// below line is for setting table name.
@Entity(tableName = "fav_table",indices = {@Index(value = {"movieID"},
        unique = true)})
public class FavModal implements Serializable {

    // below line is to auto increment
    // id for each course.
    @PrimaryKey(autoGenerate = true)

    // variable for our id.
    private int id;
    private String Name;
    private String Description;
    private String poster_path;
    private String rating;
    private String year;
    private String language;
    private String type;
    private int movieID;

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    // below line is use
    // for course duration.
    private String courseDuration;

    // below line we are creating constructor class.
    // inside constructor class we are not passing
    // our id because it is incrementing automatically
    public FavModal(int movieID,String Name,String Description,String poster_path,String rating,String year,String language,String type) {
        this.movieID = movieID;
        this.Name = Name;
        this.Description = Description;
        this.poster_path = poster_path;
        this.rating = rating;
        this.year = year;
        this.language = language;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRating() {
        return rating;
    }

    public String getYear() {
        return year;
    }

    public String getLanguage() {
        return language;
    }

    public String getCourseDuration() {
        return courseDuration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCourseDuration(String courseDuration) {
        this.courseDuration = courseDuration;
    }
}

