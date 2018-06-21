package ru.bmstu.queueapp.http.data;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.io.Serializable;
import java.util.ArrayList;

import ru.bmstu.queueapp.QueueApp;

public class Schedule implements Serializable {

    public Integer id;
    public Integer hostId = QueueApp.getUser().id;
    public LocalDate date;
    public ArrayList<AppointmentInterval> appointmentIntervals;
    public Period appointmentDuration;
    public String place;

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", repeatDate=" + date +
                ", appointmentIntervals=" + appointmentIntervals +
                ", appointmentDuration=" + appointmentDuration +
                ", place='" + place + '\'' +
                '}';
    }
}
