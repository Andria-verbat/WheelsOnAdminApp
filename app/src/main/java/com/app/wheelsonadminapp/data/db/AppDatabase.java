/*
 * Created by Accubits Technologies on 10/8/20 10:37 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 10/8/20 10:34 AM
 */

package com.app.wheelsonadminapp.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.app.wheelsonadminapp.util.AppConstants;


/* Class for Room database creation*/
@Database(entities = {UserEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class,
                                    AppConstants.DB_NAME)
                                    .fallbackToDestructiveMigration()
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
