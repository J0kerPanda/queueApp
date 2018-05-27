package ru.bmstu.queueapp.http.data;

import org.joda.time.LocalTime;

public class Appointment {

    public int visitorId;
    public String visitorFullName;
    public LocalTime start;
    public LocalTime end;

    public static Appointment Empty(LocalTime start, LocalTime end) {
        return new Appointment(0, null, start, end);
    }

    public Appointment(int visitorId, String visitorFullName, LocalTime start, LocalTime end) {
        this.visitorId = visitorId;
        this.visitorFullName = visitorFullName;
        this.start = start;
        this.end = end;
    }

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
                '}';
    }
}
