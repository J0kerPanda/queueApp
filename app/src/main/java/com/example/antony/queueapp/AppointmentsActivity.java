package com.example.antony.queueapp;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.antony.queueapp.http.data.Schedule;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.function.Consumer;

public class AppointmentsActivity extends AppCompatActivity {

    public static final String DATE_EXTRA = "DATE";
    public static final String HOST_ID_EXTRA = "HOST_ID";
    public static final String SCHEDULES_EXTRA = "SCHEDULES";

    private LocalDate date;
    private Integer hostId;
    private ArrayList<Schedule> schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_appointments);

        date = (LocalDate) getIntent().getSerializableExtra(DATE_EXTRA);
        hostId = (Integer) getIntent().getSerializableExtra(HOST_ID_EXTRA);
        schedules = (ArrayList<Schedule>) getIntent().getSerializableExtra(SCHEDULES_EXTRA);
        Log.d("MY_CUSTOM_LOG", schedules.toString());
    }
}
