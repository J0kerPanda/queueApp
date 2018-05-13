package com.example.antony.queueapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.antony.queueapp.http.ApiHttpClient;
import com.example.antony.queueapp.http.ResponseHandler;
import com.example.antony.queueapp.http.data.AppointmentData;
import com.example.antony.queueapp.http.data.HostData;
import com.example.antony.queueapp.http.data.Schedule;
import com.example.antony.queueapp.http.request.AppointmentsRequest;

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

    private void requestAppointments() {
        boolean custom = schedules.get(0).isCustom;
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i < ids.size(); ++i) {
            ids.add(schedules.get(0).rootId);
        }
        AppointmentsRequest req = new AppointmentsRequest(host.id, date, ids, custom);
        Log.d("MY_CUSTOM_LOG", req.toString());
        ApiHttpClient.getAppointments(getApplicationContext(), req, new ResponseHandler<ArrayList<AppointmentData>>() {
            @Override
            public void handle(ArrayList<AppointmentData> result) {
                Log.d("MY_CUSTOM_LOG", result.toString());
            }
        });
    }
}
