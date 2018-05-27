package ru.bmstu.queueapp.http.data;

import java.io.Serializable;

public class UserData implements Serializable {

    public int id;
    public String email;
    public String firstName;
    public String surname;
    public String patronymic;
    public boolean isHost;

    public String fullName() {
        return String.format("%s %s. %s.", surname, firstName.charAt(0), patronymic.charAt(0));
    }

    //spinner display
    @Override
    public String toString() {
        return fullName();
    }
}
