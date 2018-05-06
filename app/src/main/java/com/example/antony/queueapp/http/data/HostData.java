package com.example.antony.queueapp.http.data;

public class HostData {

    public final int id;
    public final String firstName;
    public final String surname;
    public final String patronymic;

    public HostData(int id, String firstName, String surname, String patronymic) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.patronymic = patronymic;
    }

    public String fullName() {
        return String.format("%s %s. %s.", surname, firstName.charAt(0), patronymic.charAt(0));
    }

    //spinner display
    @Override
    public String toString() {
        return fullName();
    }
}
