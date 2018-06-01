package ru.bmstu.queueapp.http.data;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import java.io.Serializable;

public class Schedule implements Serializable {

    public int id;
    public LocalDate date;
    public LocalTime start;
    public LocalTime end;
    public Period appointmentDuration;
    public String place;

    @Override
    public String toString() {
        return "Schedule{" +
                "rootId=" + id +
                ", date=" + date +
                ", start=" + start +
                ", end=" + end +
                ", appointmentDuration=" + appointmentDuration +
                ", place='" + place + '\'' +
                '}';
    }
}
