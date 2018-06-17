package ru.bmstu.queueapp.http.data;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import java.io.Serializable;
import java.util.ArrayList;

public class GenericSchedule implements Serializable {

    public Integer id;
    public ArrayList<AppointmentInterval> appointmentIntervals;
    public Period appointmentDuration;
    public String place;

    public String timeIntervals() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < appointmentIntervals.size(); ++i) {
            AppointmentInterval interval = appointmentIntervals.get(i);
            builder.append(String.format("%s - %s", interval.start.toString("HH:mm"), interval.end.toString("HH:mm")));
            if (i != appointmentIntervals.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return "GenericSchedule{" +
                "id=" + id +
                ", appointmentIntervals=" + appointmentIntervals +
                ", appointmentDuration=" + appointmentDuration +
                ", place='" + place + '\'' +
                '}';
    }
}
