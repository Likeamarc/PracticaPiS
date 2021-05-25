package com.example.practicapis.viewmodel;
/*

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import androidx.databinding.library.baseAdapters.BR;
import com.example.practicapis.Model.Login;


public class LoginViewModel extends BaseObservable {

    private Login login;

    private String successMessage ="Login successful";
    private String errorMessage = "Email or Password is not valid";

    @Bindable
    private String toastMessage = null;

    public String getToastMessage(){
        return toastMessage;
    }

    private void setToastMessage(String toastMessage){
        this.toastMessage = toastMessage;
        //notifyPropertyChanged(BR.toastMessage);
    }

    public String getUserName(){
        return login.getUsername();
    }

    private void setUserName(String username){
        login.setUsername(username);
    }

    @Bindable
    public String getUserEmail(){
        return login.getEmail();
    }

    private void setUserEmail(String email){
        login.setEmail(email);
        //notifyPropertyChanged(BR.userEmail);
    }

    @Bindable
    public String getUserPassword(){
        return login.getPassword();
    }

    private void setUserPassword(String password){
        login.setPassword(password);
        //notifyPropertyChanged(BR.userPassword);
    }

    public LoginViewModel(){
        login = new Login();
    }

    public void onButtonClicked(){
        if (isValid()){
            setToastMessage(successMessage);
        }
        else{
            setToastMessage(errorMessage);
        }
    }

    public boolean isValid(){
        return true;
        /*
        Aquí hay que hacer una funcion que constrante los datos con la Firebase y si la combinacion usuario / pass existe permitir el login, si no no.


    }
}
*/

