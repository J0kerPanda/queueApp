package ru.bmstu.queueapp.http.data;

import org.joda.time.LocalTime;

public class Appointment {

    public Integer id;
    public Integer scheduleId;
    public Integer visitorId;
    public String visitorFirstName;
    public String visitorSurname;
    public String visitorPatronymic;
    public LocalTime start;
    public LocalTime end;
    public Boolean visited;

    public static Appointment Empty(int scheduleId, LocalTime start, LocalTime end) {
        return new Appointment(scheduleId,null, null, null, null, start, end, false);
    }

    public Appointment(Integer scheduleId, Integer visitorId, String visitorFirstName, String visitorSurname, String visitorPatronymic, LocalTime start, LocalTime end, Boolean visited) {
        this.scheduleId = scheduleId;
        this.visitorId = visitorId;
        this.visitorFirstName = visitorFirstName;
        this.visitorSurname = visitorSurname;
        this.visitorPatronymic = visitorPatronymic;
        this.start = start;
        this.end = end;
        this.visited = false;
    }

    public String timeInterval() {
        return String.format("%s - %s", start.toString("HH:mm"), end.toString("HH:mm"));
    }

    public String visitorFullName() {
        return (visitorId != null) ?
            String.format("%s %s. %s.", visitorSurname, visitorFirstName.charAt(0), visitorPatronymic.charAt(0)) :
            null;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                ", id=" + id +
                ", scheduleId=" + scheduleId +
                ", visitorId=" + visitorId +
                ", visitorFirstName='" + visitorFirstName + '\'' +
                ", visitorSurname='" + visitorSurname + '\'' +
                ", visitorPatronymic='" + visitorPatronymic + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", visited " + visited +
                '}';
    }
}
