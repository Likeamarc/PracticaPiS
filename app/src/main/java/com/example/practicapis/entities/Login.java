package com.example.practicapis.entities;

import androidx.annotation.Nullable;

public class Login {

    @Nullable
    String username, email, password;

    public Login(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }
}
