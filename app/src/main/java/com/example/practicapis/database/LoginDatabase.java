package com.example.practicapis.database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.practicapis.dao.LoginDao;
import com.example.practicapis.Model.Login;


@Database(entities = Login.class, version = 1, exportSchema = false)
public abstract class LoginDatabase extends RoomDatabase {

    private static LoginDatabase loginDatabase;

    public static synchronized LoginDatabase getDatabase(Context context){
        if(loginDatabase == null){
            loginDatabase = Room.databaseBuilder(
                    context,
                    LoginDatabase.class,
                    "login_db"
            ).build();
        }
        return loginDatabase;
    }

    public abstract LoginDao loginDao();
}
