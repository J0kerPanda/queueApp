package com.example.antony.queueapp.http.request;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class CreateAppointmentRequest {

    public int hostId;
    public int visitorId;
    public int scheduleId;
    public boolean isCustom;
    public LocalDate date;
    public LocalTime start;

    @Override
    public String toString() {
        return "CreateAppointmentRequest{" +
                "hostId=" + hostId +
                ", visitorId=" + visitorId +
                ", scheduleId=" + scheduleId +
                ", isCustom=" + isCustom +
                ", date=" + date +
                ", start=" + start +
                '}';
    }
}
