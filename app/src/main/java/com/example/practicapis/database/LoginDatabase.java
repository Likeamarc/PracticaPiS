package com.example.practicapis.database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.practicapis.dao.LoginDao;
import com.example.practicapis.entities.Login;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = Login.class, version = 2, exportSchema = false)
public abstract class LoginDatabase extends RoomDatabase {

    private static final String DB_NAME = "login_db";
    private static LoginDatabase instance;

    public static synchronized LoginDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), LoginDatabase.class, DB_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract LoginDao loginDao();

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
}