package com.movies.movierow.Controllers;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.movies.movierow.API.Dao;
import com.movies.movierow.Activities.AboutMovie;
import com.movies.movierow.DB.FavDatabase;
import com.movies.movierow.Models.FavModal;

import java.util.List;

public class FavRepo {

    // below line is the create a variable
    // for dao and list for all courses.
    private Dao dao;
    private List<FavModal> allFavs;
    private FavModal fav;

    // creating a constructor for our variables
    // and passing the variables to it.
    public FavRepo(Application application,String type,int id) {
        FavDatabase database = FavDatabase.getInstance(application);
        dao = database.Dao();
        allFavs = dao.getAllFavs(type);
        if(id != 0) {
            fav = dao.getFav(id);
        }
    }

    // creating a method to insert the data to our database.
    public void insert(FavModal model) {
        new InsertFavsAsyncTask(dao).execute(model);
    }

    // creating a method to update data in database.
    public void update(FavModal model) {
        new UpdateFavsAsyncTask(dao).execute(model);
    }

    // creating a method to delete the data in our database.
    public void delete(FavModal model) {
        new DeleteFavsAsyncTask(dao).execute(model);
    }

    // below is the method to delete all the courses.
    public void deleteAllFavs() {
        new DeleteAllFavsAsyncTask(dao).execute();
    }

    // below method is to read all the courses.
    public List<FavModal> getAllFavs(String mv) {
        return allFavs;
    }

    //read specific favorite
    public FavModal getFav(int id){return fav;}

    // we are creating a async task method to insert new course.
    private static class InsertFavsAsyncTask extends AsyncTask<FavModal, Void, Void> {
        private Dao dao;

        private InsertFavsAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FavModal... model){
            // below line is use to insert our modal in dao.
            try {
                dao.insert(model[0]);
            }
            catch (Exception ex){

            }

            return null;
        }
    }

    // we are creating a async task method to update our course.
    private static class UpdateFavsAsyncTask extends AsyncTask<FavModal, Void, Void> {
        private Dao dao;

        private UpdateFavsAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FavModal... models) {
            // below line is use to update
            // our modal in dao.
            dao.update(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete course.
    private static class DeleteFavsAsyncTask extends AsyncTask<FavModal, Void, Void> {
        private Dao dao;

        private DeleteFavsAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FavModal... models) {
            // below line is use to delete
            // our course modal in dao.
            dao.delete(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete all courses.
    private static class DeleteAllFavsAsyncTask extends AsyncTask<Void, Void, Void> {
        private Dao dao;
        private DeleteAllFavsAsyncTask(Dao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // on below line calling method
            // to delete all courses.
            dao.deleteAllFavs();
            return null;
        }
    }
}

