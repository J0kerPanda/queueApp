package com.example.antony.queueapp.util.intent.data;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.io.Serializable;
import java.util.ArrayList;

public class ScheduleDatesData implements Serializable {

    public final Period period;
    public final ArrayList<LocalDate> defaultDates;
    public final ArrayList<LocalDate> customDates;

    public ScheduleDatesData(Period period, ArrayList<LocalDate> defaultDates, ArrayList<LocalDate> customDates) {
        this.period = period;
        this.defaultDates = defaultDates;
        this.customDates = customDates;
    }
}
