package com.example.practicapis.activities;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.practicapis.R;
import com.example.practicapis.database.LoginDatabase;
import com.example.practicapis.entities.Login;
import com.example.practicapis.viewmodel.LoginViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    public LoginViewModel loginViewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button buttonRegister, buttonSingIn;
        super.onCreate(savedInstanceState);
        loginViewModel = LoginViewModel.get(getApplication());

        /*Twitter.initialize(this);*/

        setContentView(R.layout.activity_main);

        buttonRegister = findViewById(R.id.registerButton);
        buttonSingIn = findViewById(R.id.signInButton);

        buttonSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAccount();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, activity_register.class));
            }
        });

    }

    private void checkAccount(){
        Login mainUser = null;
        EditText loginName = findViewById(R.id.user);
        EditText password = findViewById(R.id.password);

        String loginNameText = loginName.getText().toString();
        String passwordText = password.getText().toString();

        List<Login> userLogin = loginViewModel.getUsersWithUsername(loginNameText);
        
        if(userLogin == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("ERROR");
            builder.setMessage("El username/email y/o la contraseña no coinciden o no existen.");
            builder.setPositiveButton("Aceptar", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        try{
            if(userLogin != null){
                mainUser = userLogin.get(0);
            }
        }
        catch(IndexOutOfBoundsException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("ERROR");
            builder.setMessage("El username/email y/o la contraseña no coinciden o no existen.");
            builder.setPositiveButton("Aceptar", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        

        if(mainUser.getPassword().equals(passwordText)){
            loginViewModel.currentUser = mainUser;
            startActivity(new Intent(MainActivity.this, HomePage.class));
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("ERROR");
            builder.setMessage("El username/email y/o la contraseña no coinciden o no existen.");
            builder.setPositiveButton("Aceptar", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}