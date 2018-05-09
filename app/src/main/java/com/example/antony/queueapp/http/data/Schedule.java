package com.example.antony.queueapp.http.data;

import com.google.gson.annotations.JsonAdapter;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import java.io.Serializable;

public class Schedule implements Serializable {

    public Integer hostId;
    public LocalDate date;
    public LocalTime start;
    public LocalTime end;
    public Period appointmentDuration;
    public String place;
    public Period period;
}
