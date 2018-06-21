package ru.bmstu.queueapp.http.data;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.io.Serializable;
import java.util.ArrayList;

import ru.bmstu.queueapp.QueueApp;

public class RepeatedSchedule implements Serializable {

    public Integer id;
    public Integer hostId = QueueApp.getUser().id;
    public LocalDate repeatDate;
    public Period repeatPeriod;
    public ArrayList<AppointmentInterval> appointmentIntervals;
    public Period appointmentDuration;
    public String place;

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", repeatDate=" + repeatDate +
                ", appointmentIntervals=" + appointmentIntervals +
                ", appointmentDuration=" + appointmentDuration +
                ", place='" + place + '\'' +
                '}';
    }
}
