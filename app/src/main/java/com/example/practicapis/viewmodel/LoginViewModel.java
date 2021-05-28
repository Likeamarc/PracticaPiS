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
import java.util.concurrent.Executors;


public class LoginViewModel extends AndroidViewModel {
    private static LoginViewModel vmInstance;
    private LoginDatabase loginDatabase;
    public List<Login> usersListLiveData;
    public Login currentUser;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        currentUser = null;
        loginDatabase = LoginDatabase.getInstance(application);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                usersListLiveData = loginDatabase.loginDao().getAllUsers();
            }
        });

    }

    @MainThread
    public static LoginViewModel get(@NonNull Application application){
        if(vmInstance == null){
            vmInstance = new LoginViewModel(application);
        }
        return vmInstance;
    }


    public List<Login> getUsersListLiveData(){
        usersListLiveData = loginDatabase.loginDao().getAllUsers();
        return usersListLiveData;

    }

    public List<Login> getUsersWithEmail(String search){
        return loginDatabase.loginDao().findUserWithEmail(search);
    }

    public List<Login> getUsersWithUsername(String search){
        return loginDatabase.loginDao().findUserWithName(search);
    }

    public void insertUser(Login login){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                loginDatabase.loginDao().insertUser(login);
            }
        });
    }

    public void deleteUser(Login login){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                loginDatabase.loginDao().deleteUser(login);
            }
        });
    }

    public void updateUser(Login login){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                loginDatabase.loginDao().updateUser(login);
            }
        });
    }


}
