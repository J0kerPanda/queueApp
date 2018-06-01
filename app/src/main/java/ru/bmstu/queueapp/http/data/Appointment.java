package ru.bmstu.queueapp.http.data;

import org.joda.time.LocalTime;

public class Appointment {

    public int visitorId;
    public String visitorFirstName;
    public String visitorSurname;
    public String visitorPatronymic;
    public LocalTime start;
    public LocalTime end;

    public static Appointment Empty(LocalTime start, LocalTime end) {
        return new Appointment(-1, null, null, null, start, end);
    }

    public Appointment(int visitorId, String visitorFirstName, String visitorSurname, String visitorPatronymic, LocalTime start, LocalTime end) {
        this.visitorId = visitorId;
        this.visitorFirstName = visitorFirstName;
        this.visitorSurname = visitorSurname;
        this.visitorPatronymic = visitorPatronymic;
        this.start = start;
        this.end = end;
    }

    public String timeInterval() {
        return String.format("%s - %s", start.toString("HH:mm"), end.toString("HH:mm"));
    }

    public String visitorFullName() {
        return (visitorId > 0) ?
                String.format("%s %s. %s.", visitorSurname, visitorFirstName.charAt(0), visitorPatronymic.charAt(0)) :
                null;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "visitorId=" + visitorId +
                ", visitorFirstName='" + visitorFirstName + '\'' +
                ", visitorSurname='" + visitorSurname + '\'' +
                ", visitorPatronymic='" + visitorPatronymic + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
