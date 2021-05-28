package com.example.practicapis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.practicapis.R;
import com.example.practicapis.entities.Login;
import com.example.practicapis.viewmodel.LoginViewModel;

public class activity_register extends AppCompatActivity {

    public LoginViewModel loginViewModel;
    public static final int REQUEST_USER_PASSWORD = 1;



    public void onCreate(Bundle savedInstanceState) {
        Button registerButton, backButton;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginViewModel = LoginViewModel.get(getApplication());
        ImageView imageBack = findViewById(R.id.registerBackButton);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_register.this, MainActivity.class));
            }
        });
        registerButton = (Button) findViewById(R.id.applyChanges);
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
        EditText firstname =  findViewById(R.id.firstName);
        EditText lastname = findViewById(R.id.lastName);
        EditText username = findViewById(R.id.username);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.registerPassword);
        EditText repeatPassword = findViewById(R.id.repeatPassword);

        String firstNameText = firstname.getText().toString();
        String lastNameText = lastname.getText().toString();
        String usernameText = username.getText().toString();
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        String repeatText = repeatPassword.getText().toString();

        if(!passwordText.equals(repeatText)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ERROR");
            builder.setMessage("Las contraseñas no coinciden!");
            builder.setPositiveButton("Aceptar", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        Login user = new Login(usernameText, firstNameText, lastNameText, passwordText, emailText);
        loginViewModel.insertUser(user);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ÉXITO");
        builder.setMessage("Cuenta creada correctamente.");
        builder.setPositiveButton("Aceptar", null);

        AlertDialog dialog = builder.create();
        startActivity(new Intent(activity_register.this, MainActivity.class));
        dialog.show();
    }


}


