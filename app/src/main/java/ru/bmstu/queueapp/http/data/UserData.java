package ru.bmstu.queueapp.http.data;

import java.io.Serializable;

public class UserData implements Serializable {

    public int id;
    public String email;
    public String firstName;
    public String surname;
    public String patronymic;
    public boolean isHost;

    public UserData() {}

    public UserData(int id, String email, String firstName, String surname, String patronymic, boolean isHost) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.patronymic = patronymic;
        this.isHost = isHost;
    }

    public String fullName() {
        return String.format("%s %s. %s.", surname, firstName.charAt(0), patronymic.charAt(0));
    }

    public String fullData() {
        return "UserData{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", isHost=" + isHost +
                '}';
    }

    //spinner display
    @Override
    public String toString() {
        return fullName();
    }
}
