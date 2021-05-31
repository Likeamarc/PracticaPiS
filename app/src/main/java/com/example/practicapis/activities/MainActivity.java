package com.example.practicapis.activities;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;

import com.example.practicapis.R;
import com.example.practicapis.viewmodel.LoginViewModel;

import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    public LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button buttonRegister, buttonSingIn;
        super.onCreate(savedInstanceState);
        try {
            loginViewModel = new LoginViewModel(getApplication());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);


        if(loginViewModel.usersListLiveData.size() > 0){
            startActivity(new Intent(MainActivity.this, HomePage.class));
        }else{
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

    }

}