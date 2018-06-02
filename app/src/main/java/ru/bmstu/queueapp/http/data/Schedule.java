package ru.bmstu.queueapp.http.data;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import java.io.Serializable;
import java.util.ArrayList;

public class Schedule implements Serializable {

    public int id;
    public LocalDate date;
    public ArrayList<AppointmentInterval> appointmentIntervals;
    public Period appointmentDuration;
    public String place;

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", date=" + date +
                ", appointmentIntervals=" + appointmentIntervals +
                ", appointmentDuration=" + appointmentDuration +
                ", place='" + place + '\'' +
                '}';
    }
}
