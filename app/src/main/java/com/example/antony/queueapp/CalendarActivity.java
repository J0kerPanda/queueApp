package com.example.antony.queueapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.antony.queueapp.data.ScheduleDatesData;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {

    public static final String SCHEDULE_DATES_EXTRA = "SCHEDULE_DATES";

    private MaterialCalendarView calendarView;
    private ScheduleDatesData datesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        datesData = (ScheduleDatesData) getIntent().getSerializableExtra(SCHEDULE_DATES_EXTRA);
        Log.d("MY_CUSTOM_LOG", datesData.defaultDates.toString());
        Log.d("MY_CUSTOM_LOG", datesData.customDates.toString());

        LocalDate today = new LocalDate();

        calendarView.clearSelection();
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        calendarView.state()
                .edit()
                .setMinimumDate(today.toDate())
                .setMaximumDate(today.plus(datesData.period.toStandardDays()).toDate())
                .commit();

        selectDates(datesData.defaultDates);
        selectDates(datesData.customDates);
    }

    private void selectDates(ArrayList<LocalDate> dates) {
        for (int i = 0; i < dates.size(); ++i) {
            calendarView.setDateSelected(dates.get(i).toDate(), true);
        }
    }
}
