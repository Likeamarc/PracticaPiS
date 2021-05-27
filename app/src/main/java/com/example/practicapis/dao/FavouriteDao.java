package com.example.practicapis.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.practicapis.entities.Note;

import java.util.List;

@Dao
public interface FavouriteDao {

    @Query("SELECT * FROM notes ORDER BY favourite DESC" )
    List<Note> getAllFavourites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote (Note note);

    @Delete
    void deleteNote (Note note);

}
