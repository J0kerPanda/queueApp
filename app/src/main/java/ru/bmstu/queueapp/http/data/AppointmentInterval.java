package ru.bmstu.queueapp.http.data;

import org.joda.time.LocalTime;

import java.io.Serializable;

public class AppointmentInterval implements Serializable {

    public LocalTime start;
    public LocalTime end;

    @Override
    public String toString() {
        return String.format("%s - %s", start.toString("HH:mm"), end.toString("HH:mm"));
    }
}
