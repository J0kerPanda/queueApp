package ru.bmstu.queueapp.http.request;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class CreateAppointmentRequest {

    public int scheduleId;
    public int visitorId;
    public LocalDate date;
    public LocalTime start;
    public LocalTime end;

    public CreateAppointmentRequest(int scheduleId, int visitorId, LocalDate date, LocalTime start, LocalTime end) {
        this.scheduleId = scheduleId;
        this.visitorId = visitorId;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "CreateAppointmentRequest{" +
                "hostId=" + scheduleId +
                ", visitorId=" + visitorId +
                ", date=" + date +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
