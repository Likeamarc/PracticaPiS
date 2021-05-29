package com.example.practicapis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.practicapis.R;
import com.example.practicapis.entities.Login;
import com.example.practicapis.viewmodel.LoginViewModel;
import com.google.android.material.navigation.NavigationView;

public class activity_settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    public LoginViewModel loginViewModel;


    public void onCreate(Bundle savedInstanceState) {
        Button registerButton, backButton;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(findViewById(R.id.toolbar));

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);



        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        loginViewModel = LoginViewModel.get(getApplication());
        ImageView imageBack = findViewById(R.id.registerBackButton);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_settings.this, MainActivity.class));
            }
        });
        registerButton = (Button) findViewById(R.id.applyChanges);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked button");
                try{
                    updateUser(v);
                }
                catch(NullPointerException e){
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity_settings.this);
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

    public void updateUser(View v){
        EditText firstname =  findViewById(R.id.firstName);
        EditText lastname = findViewById(R.id.lastName);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.registerPassword);
        EditText repeatPassword = findViewById(R.id.repeatPassword);

        String firstNameText,lastNameText, emailText, passwordText = null;

        if(firstname.getText().toString().equals("")){firstNameText = loginViewModel.currentUser.getFirstname();}
        else{
            firstNameText = firstname.getText().toString();
        }

        if(lastname.getText().toString().equals("")){lastNameText = loginViewModel.currentUser.getLastname();}
        else{
            lastNameText = lastname.getText().toString();
        }
        if(email.getText().toString().equals("")){emailText = loginViewModel.currentUser.getEmail();}
        else{
            emailText = email.getText().toString();
        }
        if(!password.getText().toString().equals("")){
            if(repeatPassword.getText().toString().equals("") || !repeatPassword.getText().equals(password.getText())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity_settings.this);
                builder.setTitle("ERROR");
                builder.setMessage("Las contraseñas no coinciden!");
                builder.setPositiveButton("Aceptar", null);

                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
            else{
                passwordText = password.getText().toString();
            }
        }
        else if(password.getText().equals("")){
            passwordText = loginViewModel.currentUser.getPassword();
        }

        Login user = new Login(loginViewModel.currentUser.getUsername(), firstNameText, lastNameText, passwordText, emailText);
        loginViewModel.updateUser(user);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ÉXITO");
        builder.setMessage("Cuenta modificada correctamente");
        builder.setPositiveButton("Aceptar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
        startActivity(new Intent(activity_settings.this, MainActivity.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int title;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(activity_settings.this, HomePage.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(activity_settings.this, activity_settings.class));
                break;
            case R.id.nav_logout:
                loginViewModel.currentUser = null;
                startActivity(new Intent(activity_settings.this, MainActivity.class));
                break;
            default:
                throw new IllegalArgumentException("menu option not implemented!!");
        }

        return true;
    }


}


