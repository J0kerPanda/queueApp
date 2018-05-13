package com.example.antony.queueapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.antony.queueapp.http.data.HostData;
import com.example.antony.queueapp.http.data.Schedule;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class AppointmentsActivity extends AppCompatActivity {

    public static final String DATE_EXTRA = "DATE";
    public static final String HOST_EXTRA = "HOST";
    public static final String SCHEDULES_EXTRA = "SCHEDULES";

    private LocalDate date;
    private HostData host;
    private ArrayList<Schedule> schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        date = (LocalDate) getIntent().getSerializableExtra(DATE_EXTRA);
        host = (HostData) getIntent().getSerializableExtra(HOST_EXTRA);
        schedules = (ArrayList<Schedule>) getIntent().getSerializableExtra(SCHEDULES_EXTRA);
        Log.d("MY_CUSTOM_LOG", schedules.toString());
        ((TextView) findViewById(R.id.appointmentDateText)).setText(date.toString());
        ((TextView) findViewById(R.id.appointmentHostText)).setText(host.fullName());
    }
}
