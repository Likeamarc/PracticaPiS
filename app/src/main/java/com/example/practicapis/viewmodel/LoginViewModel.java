package com.example.practicapis.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.practicapis.database.LoginDatabase;
import com.example.practicapis.database.NoteDatabase;
import com.example.practicapis.entities.Login;
import com.example.practicapis.entities.Note;

import java.util.List;


public class LoginViewModel extends AndroidViewModel {
    private LoginDatabase loginDatabase;
    public LiveData<List<Login>> usersListLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginDatabase = LoginDatabase.getInstance(application);
        usersListLiveData = loginDatabase.loginDao().getAllUsers();
    }


    public LiveData<List<Login>> getUsersListLiveData(){
        return usersListLiveData;
    }

    public void insertUser(Login login){
        loginDatabase.loginDao().insertUser(login);
    }

    public void deleteUser(Login login){
        loginDatabase.loginDao().deleteUser(login);
    }

    public void updateUser(Login login){
        loginDatabase.loginDao().updateUser(login);
    }


}
