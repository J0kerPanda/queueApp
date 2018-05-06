package com.example.antony.queueapp.util.intent.data;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class ScheduleDatesData {

    public final ArrayList<LocalDate> defaultDates;
    public final ArrayList<LocalDate> customDates;

    public ScheduleDatesData(ArrayList<LocalDate> defaultDates, ArrayList<LocalDate> customDates) {
        this.defaultDates = defaultDates;
        this.customDates = customDates;
    }
}
