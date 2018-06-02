package ru.bmstu.queueapp.http.data;

import org.joda.time.LocalTime;

import java.io.Serializable;

public class AppointmentInterval implements Serializable {

    public LocalTime start;
    public LocalTime end;
}
