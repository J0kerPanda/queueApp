package com.example.antony.queueapp.http.data;

import org.joda.time.LocalTime;

public class Appointment {

    public int visitorId;
    public String visitorFullName;
    public LocalTime start;
    public LocalTime end;
    public String status;

    public String timeInterval() {
        return String.format("%s - %s", start.toString("HH:mm"), end.toString("HH:mm"));
    }

    @Override
    public String toString() {
        return "AppointmentData{" +
                "visitorId=" + visitorId +
                ", visitorFullName=" + visitorFullName +
                ", start=" + start +
                ", end=" + end +
                ", status='" + status + '\'' +
                '}';
    }
}
