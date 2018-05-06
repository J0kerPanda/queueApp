package com.example.antony.queueapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.joda.time.LocalDate;

public class DateAppointmentsActivity extends AppCompatActivity {

    public static final String DATE_EXTRA = "APPOINTMENTS_DATE";
    public static final String HOST_ID_EXTRA = "HOST_ID";

    private LocalDate date;
    private Integer hostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_appointments);

        date = (LocalDate) getIntent().getSerializableExtra(DATE_EXTRA);
        hostId = getIntent().getIntExtra(HOST_ID_EXTRA, -1);
        Log.d("MY_CUSTOM_LOG", date.toString());
        Log.d("MY_CUSTOM_LOG", hostId.toString());
    }
}
