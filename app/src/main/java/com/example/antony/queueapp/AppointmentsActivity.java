package com.example.antony.queueapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
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

    private ListView appointmentsView;

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

        ((TextView) findViewById(R.id.appointmentDateText)).setText(date.toString());
        ((TextView) findViewById(R.id.appointmentHostText)).setText(host.fullName());
        appointmentsView = findViewById(R.id.appointmentsView);

        requestAppointments();

        Log.d("MY_CUSTOM_LOG", schedules.toString());
    }

    private void requestAppointments() {
        boolean custom = schedules.get(0).isCustom;
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i < schedules.size(); ++i) {
            Log.i("MY_CUSTOM_LOG", String.valueOf(schedules.get(i).rootId));
            ids.add(schedules.get(i).rootId);
        }
        AppointmentsRequest req = new AppointmentsRequest(host.id, date, ids, custom);
        Log.i("MY_CUSTOM_LOG", req.toString());
        ApiHttpClient.getAppointments(getApplicationContext(), req, new ResponseHandler<ArrayList<AppointmentData>>() {
            @Override
            public void handle(ArrayList<AppointmentData> result) {
                ArrayList<String> strings = new ArrayList<>(result.size());
                for (int i = 0; i < result.size(); ++i) {
                    AppointmentData data = result.get(i);
                    strings.add(String.format("%s - %s", data.start.toString(), data.end.toString()));
                }

                ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.appointment_item_display, strings);
                appointmentsView.setAdapter(adapter);

                Log.d("MY_CUSTOM_LOG", result.toString());
            }
        });
    }
}
