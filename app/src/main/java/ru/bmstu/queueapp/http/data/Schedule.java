package ru.bmstu.queueapp.http.data;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.ArrayList;

public class Schedule {

    public int hostId;
    public LocalDate date;
    public ArrayList<AppointmentInterval> appointmentIntervals;
    public Period appointmentDuration;
    public String place;

    @Override
    public String toString() {
        return "Schedule{" +
                "hostId=" + hostId +
                ", date=" + date +
                ", appointmentIntervals=" + appointmentIntervals +
                ", appointmentDuration=" + appointmentDuration +
                ", place='" + place + '\'' +
                '}';
    }
}
