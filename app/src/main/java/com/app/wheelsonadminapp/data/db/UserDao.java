package com.app.wheelsonadminapp.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


/* Data access object for Room Database*/
@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<UserEntity> getAll();

    @Query("SELECT * FROM user WHERE uid =:uid")
    UserEntity getUserById(int uid);

    @Query("SELECT * FROM user WHERE uid =:userId")
    LiveData<UserEntity> getUser(int userId);

    @Insert
    void insertUser(UserEntity userEntity);

    @Update
    void updateUser(UserEntity userEntity);

    @Delete
    void delete(UserEntity userEntity);

    @Query("DELETE FROM user")
    void deleteAllUsers();
}
