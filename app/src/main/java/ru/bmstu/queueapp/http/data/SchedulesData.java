package ru.bmstu.queueapp.http.data;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.io.Serializable;
import java.util.HashMap;

public class SchedulesData implements Serializable {

    public Period period;
    public HashMap<LocalDate, GenericSchedule> schedules;

    public SchedulesData(Period period, HashMap<LocalDate, GenericSchedule> schedules) {
        this.period = period;
        this.schedules = schedules;
    }

    @Override
    public String toString() {
        return "ScheduleData{" +
                "period=" + period +
                ", schedules=" + schedules +
                '}';
    }
}
