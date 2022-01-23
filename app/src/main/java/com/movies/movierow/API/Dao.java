package com.movies.movierow.API;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.movies.movierow.Models.FavModal;

import java.util.List;

// Adding annotation
// to our Dao class
@androidx.room.Dao
public interface Dao {

    // below method is use to
    // add data to database.
    @Insert
    void insert(FavModal model);

    // below method is use to update
    // the data in our database.
    @Update
    void update(FavModal model);

    // below line is use to delete a
    // specific course in our database.
    @Delete
    void delete(FavModal model);

    // on below line we are making query to
    // delete all courses from our database.
    @Query("DELETE FROM fav_table")
    void deleteAllFavs();

    // below line is to read all the courses from our database.
    // in this we are ordering our courses in ascending order
    // with our course name.
    @Query("SELECT * FROM fav_table WHERE type =:cat ORDER BY year DESC")
    List<FavModal> getAllFavs(String cat);

    @Query("SELECT * FROM fav_table WHERE movieID =:ID")
    FavModal getFav(int ID);


}

