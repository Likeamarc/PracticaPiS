package com.example.practicapis.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.practicapis.entities.Login;

import java.util.List;

@Dao
public interface LoginDao {

    @Query("SELECT * FROM login" )
    List<Login> getAllUsers();

    @Query("SELECT * FROM login WHERE username = :search")
    public List<Login> findUserWithName(String search);

    @Query("SELECT * FROM login WHERE email = :search")
    public List<Login> findUserWithEmail(String search);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser (Login user);

    @Delete
    void deleteUser (Login user);

    @Update
    void updateUser (Login user);


}

