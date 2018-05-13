package com.example.antony.queueapp.http.request;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class AppointmentsRequest {

    public int hostId;
    public LocalDate date;
    public ArrayList<Integer> scheduleIds;
    public boolean isCustom;

    public AppointmentsRequest(int hostId, LocalDate date, ArrayList<Integer> scheduleIds, boolean isCustom) {
        this.hostId = hostId;
        this.date = date;
        this.scheduleIds = scheduleIds;
        this.isCustom = isCustom;
    }
}
