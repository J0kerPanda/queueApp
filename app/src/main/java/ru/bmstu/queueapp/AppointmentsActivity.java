package ru.bmstu.queueapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import ru.bmstu.queueapp.http.ApiHttpClient;
import ru.bmstu.queueapp.http.ResponseHandler;
import ru.bmstu.queueapp.http.data.Appointment;
import ru.bmstu.queueapp.http.data.UserData;
import ru.bmstu.queueapp.http.data.Schedule;
import ru.bmstu.queueapp.http.request.CreateAppointmentRequest;
import ru.bmstu.queueapp.util.adapter.AppointmentItemAdapter;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AppointmentsActivity extends AppCompatActivity {

    public static final String DATE_EXTRA = "DATE";
    public static final String HOST_EXTRA = "HOST";
    public static final String SCHEDULES_EXTRA = "SCHEDULES";

    private ListView appointmentsListView;

    private LocalDate date;
    private UserData host;
    private ArrayList<Schedule> schedules;
    private HashMap<LocalTime, Appointment> appointmentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        date = (LocalDate) getIntent().getSerializableExtra(DATE_EXTRA);
        host = (UserData) getIntent().getSerializableExtra(HOST_EXTRA);
        schedules = (ArrayList<Schedule>) getIntent().getSerializableExtra(SCHEDULES_EXTRA);
        appointmentMap = generateAppointments(schedules);

        ((TextView) findViewById(R.id.appointmentDateText)).setText(date.toString());
        ((TextView) findViewById(R.id.appointmentHostText)).setText(host.fullName());
        appointmentsListView = findViewById(R.id.appointmentsView);
        appointmentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Appointment appointment = (Appointment) parent.getItemAtPosition(position);
                //todo check if not taken
                //todo self.id as visitorid
                createAppointment(appointment);
            }
        });

        requestAppointments();

        Log.d("MY_CUSTOM_LOG", schedules.toString());
    }

    private HashMap<LocalTime, Appointment> generateAppointments(ArrayList<Schedule> schedules) {
        HashMap<LocalTime, Appointment> appointments = new HashMap<>();
        for (Schedule s: schedules) {
            for (LocalTime curr = s.start; curr.isBefore(s.end); curr = curr.plus(s.appointmentDuration)) {
                appointments.put(curr, Appointment.Empty(s.id, curr, curr.plus(s.appointmentDuration)));
            }
        }
        return appointments;
    }

    private void requestAppointments() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Schedule s: schedules) {
            ids.add(s.id);
        }
        ApiHttpClient.instance().getAppointments(ids, new ResponseHandler<ArrayList<Appointment>>() {
            @Override
            public void handle(ArrayList<Appointment> result) {
                Log.d("MY_CUSTOM_LOG", String.valueOf(result.size()));
                Log.d("MY_CUSTOM_LOG", result.toString());
                HashMap<LocalTime, Appointment> clone = (HashMap<LocalTime, Appointment>) appointmentMap.clone();
                for (Appointment a: result) {
                    clone.put(a.start, a);
                }
                AppointmentItemAdapter adapter = new AppointmentItemAdapter(getApplicationContext(), new ArrayList<>(clone.values()));
                appointmentsListView.setAdapter(adapter);
            }
        });
    }

    public void createAppointment(Appointment selected) {
        CreateAppointmentRequest req = new CreateAppointmentRequest(
            selected.scheduleId,
            QueueApp.getUser().id,
            date,
            selected.start,
            selected.end
        );

        ApiHttpClient.instance().createAppointment(req, new ResponseHandler<Boolean>() {
            @Override
            public void handle(Boolean result) {
            //todo false case
            Log.i("MY_CUSTOM_LOG", String.valueOf(result));
            if (result) {
                finish();
                startActivity(getIntent()); //todo put sorted stuff in intent?
            }
            }
        });
    }
}
