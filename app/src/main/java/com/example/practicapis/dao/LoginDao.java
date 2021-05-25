package com.example.practicapis.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.practicapis.Model.Login;

import java.util.List;

@Dao
public interface LoginDao {

    /*
    @Query("SELECT * FROM notes ORDER BY id DESC" )
    List<Login> getAllUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser (Login user);

    @Delete
    void deleteLogin (Login user);

     */
}

