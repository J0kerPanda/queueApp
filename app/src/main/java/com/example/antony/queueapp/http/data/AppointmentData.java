package com.example.antony.queueapp.http.data;

import org.joda.time.LocalTime;

public class AppointmentData {

    public int visitorId;
    public String visitorFullName;
    public LocalTime start;
    public LocalTime end;
    public String status;

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
