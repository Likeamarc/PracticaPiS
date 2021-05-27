package com.example.practicapis.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.practicapis.R;
import com.example.practicapis.entities.Login;
import com.example.practicapis.viewmodel.LoginViewModel;

public class activity_register extends AppCompatActivity {

    public LoginViewModel loginViewModel;
    public static final int REQUEST_USER_PASSWORD = 1;



    public void onCreate(Bundle savedInstanceState) {
        Button registerButton;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        ImageView imageBack = findViewById(R.id.registerBackButton);
        registerButton = (Button) findViewById(R.id.signUpButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked button");
                try{
                    createUser(v);
                }
                catch(NullPointerException e){
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity_register.this);
                    builder.setTitle("ERROR");
                    builder.setMessage("Uno de los campos esta vacio!");
                    builder.setPositiveButton("Aceptar", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }


            }
        });



    }

    public void createUser(View v){
        String firstname =  v.findViewById(R.id.firstName).toString();
        String lastname = v.findViewById(R.id.lastName).toString();
        String username = v.findViewById(R.id.username).toString();
        String email = v.findViewById(R.id.email).toString();
        String password = v.findViewById(R.id.registerPassword).toString();
        String repeatPassword = v.findViewById(R.id.repeatPassword).toString();

        if(!password.equals(repeatPassword)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ERROR");
            builder.setMessage("Las contraseñas no coinciden!");
            builder.setPositiveButton("Aceptar", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        Login user = new Login(username, firstname, lastname, password, email);
        loginViewModel.insertUser(user);
    }


}


