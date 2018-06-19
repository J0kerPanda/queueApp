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

    public String timeIntervals() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < appointmentIntervals.size(); ++i) {
            builder.append(appointmentIntervals.get(i).toString());
            if (i != appointmentIntervals.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

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
