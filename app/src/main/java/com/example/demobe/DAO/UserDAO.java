package com.example.demobe.DAO;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demobe.model.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void inserUser(User user);

    @Query("SELECT * FROM " + User.TABLE_NAME)
    Cursor getListUser();
//    List<User> getListUser();

    @Query("SELECT * FROM "+ User.TABLE_NAME + " where username = :username")
    Cursor checkUser(String username);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM " + User.TABLE_NAME + " WHERE id = :id")
    void deleteUserById(int id);

    @Query("DELETE FROM " + User.TABLE_NAME)
    void deleteAllUser();

    @Query("SELECT * FROM " + User.TABLE_NAME +" WHERE username LIKE '%' || :name || '%' ")
    Cursor searchUser(String name);
}
