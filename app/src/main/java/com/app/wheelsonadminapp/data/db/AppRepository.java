package com.app.wheelsonadminapp.data.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

/* Class for manipulating data in table */
public class AppRepository {

    private AppDatabase appDatabase;
    public AppRepository(Context context) {
        appDatabase = AppDatabase.getDatabase(context);
    }

    public void createUser(final UserEntity userEntity) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.userDao().insertUser(userEntity);
                return null;
            }
        }.execute();
    }

    public void updateUser(final UserEntity userEntity) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.userDao().updateUser(userEntity);
                return null;
            }
        }.execute();
    }


    public void deleteUser(final UserEntity userEntity) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.userDao().delete(userEntity);
                return null;
            }
        }.execute();
    }

    public void deleteAllUser(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.userDao().deleteAllUsers();
                return null;
            }
        }.execute();
    }


    public UserEntity getUser(){
        UserEntity userEntity = null;
        try {
            userEntity = new GetUsersAsyncTask().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userEntity;
    }

    public String getGuid(){
        UserEntity userEntity = null;
        try {
            userEntity = new GetUsersAsyncTask().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userEntity != null ? userEntity.getToken() : null;
    }


    @SuppressLint("StaticFieldLeak")
    private class GetUsersAsyncTask extends AsyncTask<Void, Void,UserEntity>
    {
        @Override
        protected UserEntity doInBackground(Void... url) {
            return appDatabase.userDao().getUserById(1);
        }
    }

    public LiveData<UserEntity> getUser(int id) {
        return appDatabase.userDao().getUser(id);
    }
}