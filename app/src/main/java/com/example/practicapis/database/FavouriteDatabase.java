package com.example.practicapis.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.practicapis.Model.Note;
import com.example.practicapis.dao.FavouriteDao;
import com.example.practicapis.dao.NoteDao;

@Database(entities = Note.class, version = 1, exportSchema = false)
public abstract class FavouriteDatabase extends RoomDatabase {

    private static FavouriteDatabase notesDatabase;

    public static synchronized FavouriteDatabase getDatabase(Context context) {
        if (notesDatabase == null) {
            notesDatabase = Room.databaseBuilder(
                    context,
                    FavouriteDatabase.class,
                    "notes_db"
            ).build();
        }
        return notesDatabase;
    }

    public abstract FavouriteDao favouriteDao();
}