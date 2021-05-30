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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class LoginViewModel extends AndroidViewModel {
    private static LoginViewModel vmInstance;
    private LoginDatabase loginDatabase;
    public List<Login> usersListLiveData;
    public Login currentUser;

    public LoginViewModel(@NonNull Application application) throws ExecutionException, InterruptedException {
        super(application);
        currentUser = null;
        loginDatabase = LoginDatabase.getInstance(application);
        getUsersListLiveData();

    }

    @MainThread
    public static LoginViewModel get(@NonNull Application application) throws ExecutionException, InterruptedException {
        if(vmInstance == null){
            vmInstance = new LoginViewModel(application);
        }
        return vmInstance;
    }


    public List<Login> getUsersListLiveData() throws ExecutionException, InterruptedException {
        Future<List<Login>> futureLoginList = Executors.newSingleThreadExecutor().submit(new Callable<List<Login>>() {
            @Override
            public List<Login> call() throws Exception {
                return loginDatabase.loginDao().getAllUsers();
            }
        });

        usersListLiveData = futureLoginList.get();
        return usersListLiveData;

    }

    public List<Login> getUsersWithEmail(String search) throws ExecutionException, InterruptedException {
        Future<List<Login>> futureEmailsList = Executors.newSingleThreadExecutor().submit(new Callable<List<Login>>() {
            @Override
            public List<Login> call() throws Exception {
                return loginDatabase.loginDao().findUserWithEmail(search);
            }
        });

        return futureEmailsList.get();
    }

    public List<Login> getUsersWithUsername(String search) throws ExecutionException, InterruptedException {
        Future<List<Login>> futureUserList = Executors.newSingleThreadExecutor().submit(new Callable<List<Login>>() {
            @Override
            public List<Login> call() throws Exception {
                return loginDatabase.loginDao().findUserWithName(search);
            }
        });

        return futureUserList.get();
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
