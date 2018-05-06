package com.example.antony.queueapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import com.example.antony.queueapp.util.intent.data.ScheduleDatesData;

public class CalendarActivity extends AppCompatActivity {

    public static final String SCHEDULE_DATES_EXTRA = "SCHEDULE_DATES";

    private CalendarView calendarView;
    private ScheduleDatesData datesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        datesData = (ScheduleDatesData) getIntent().getSerializableExtra(SCHEDULE_DATES_EXTRA);
        Log.d("MY_CUSTOM_LOG", datesData.defaultDates.toString());
        Log.d("MY_CUSTOM_LOG", datesData.customDates.toString());
    }
}
