package ru.bmstu.queueapp.http.data;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.io.Serializable;
import java.util.ArrayList;

import ru.bmstu.queueapp.QueueApp;

public class Schedule implements Serializable {

    public int hostId;
    public LocalDate date;
    public ArrayList<AppointmentInterval> appointmentIntervals;
    public Period appointmentDuration;
    public String place;

    public Schedule() {
        this.hostId = QueueApp.getUser().id;
    }

    public Schedule(LocalDate date, GenericSchedule genericSchedule) {
        this.hostId = QueueApp.getUser().id;
        this.date = date;
        this.appointmentIntervals = genericSchedule.appointmentIntervals;
        this.appointmentDuration = genericSchedule.appointmentDuration;
        this.place = genericSchedule.place;
    }

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
