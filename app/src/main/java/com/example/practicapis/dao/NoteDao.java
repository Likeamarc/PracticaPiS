package com.example.practicapis.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.practicapis.entities.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY title DESC" )
    List<Note> getAllNotes();

    @Query("SELECT * FROM notes WHERE favourite = 1")
    List<Note> getAllFavouritesNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote (Note note);

    @Delete
    void deleteNote (Note note);

    @Update
    void updateNote(Note note);

}
