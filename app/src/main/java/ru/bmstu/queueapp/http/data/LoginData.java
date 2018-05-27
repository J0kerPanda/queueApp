package ru.bmstu.queueapp.http.data;

public class LoginData {

    public String email;
    public String password;

    public LoginData() {}

    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
