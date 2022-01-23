package com.movies.movierow.Controllers;

import android.app.Application;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.movies.movierow.Models.FavModal;

import java.util.List;

public class ViewModal extends AndroidViewModel {

    // creating a new variable for course repository.
    private FavRepo repository;

    // below line is to create a variable for live
    // data where all the courses are present.
    private List<FavModal> allFavs;
    private FavModal fav;

    // constructor for our view modal.
    public ViewModal(@NonNull Application application,String type,int id) {
        super(application);
        repository = new FavRepo(application,type,id);
        allFavs = repository.getAllFavs(type);
        if(id != 0) fav = repository.getFav(id);
    }

    // below method is use to insert the data to our repository.
    public void insert(FavModal model){
        repository.insert(model);
    }

    // below line is to update data in our repository.
    public void update(FavModal model) {
        repository.update(model);
    }

    // below line is to delete the data in our repository.
    public void delete(FavModal model) {
        repository.delete(model);
    }

    // below method is to delete all the courses in our list.
    public void deleteAllFavs() {
        repository.deleteAllFavs();
    }

    // below method is to get all the courses in our list.
    public List<FavModal> getAllFavs(String movies) {
        return allFavs;
    }

    //get specific fav
    public FavModal getFav(int id){return fav;}
}

