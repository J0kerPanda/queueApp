package com.example.antony.queueapp.http.data;

public class LoginData {

    public String email;
    public String password;

    @Override
    public String toString() {
        return "LoginData{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
