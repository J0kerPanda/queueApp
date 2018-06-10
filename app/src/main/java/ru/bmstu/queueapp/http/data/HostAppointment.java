package ru.bmstu.queueapp.http.data;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class HostAppointment {

    public Integer id;
    public Integer scheduleId;
    public Integer hostId;
    public String hostFirstName;
    public String hostSurname;
    public String hostPatronymic;
    public LocalDate date;
    public LocalTime start;
    public LocalTime end;

    public String hostFullName() {
        return (hostId != null) ?
            String.format("%s %s. %s.", hostSurname, hostFirstName.charAt(0), hostPatronymic.charAt(0)) :
            null;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                "scheduleId=" + scheduleId +
                "visitorId=" + hostId +
                ", visitorFirstName='" + hostFirstName + '\'' +
                ", visitorSurname='" + hostSurname + '\'' +
                ", visitorPatronymic='" + hostPatronymic + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
