package ru.bmstu.queueapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.HashMap;

import ru.bmstu.queueapp.adapters.AppointmentItemAdapter;
import ru.bmstu.queueapp.http.ApiHttpClient;
import ru.bmstu.queueapp.http.ResponseHandler;
import ru.bmstu.queueapp.http.data.Appointment;
import ru.bmstu.queueapp.http.data.AppointmentInterval;
import ru.bmstu.queueapp.http.data.Schedule;
import ru.bmstu.queueapp.http.data.UserData;
import ru.bmstu.queueapp.http.request.CreateAppointmentRequest;

public class AppointmentsActivity extends AppCompatActivity {

    public static final String DATE_EXTRA = "DATE";
    public static final String HOST_EXTRA = "HOST";
    public static final String SCHEDULE_EXTRA = "SCHEDULE";

    private ListView appointmentsListView;

    private LocalDate date;
    private UserData host;
    private Schedule schedule;
    private HashMap<LocalTime, Appointment> appointmentMap;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        date = (LocalDate) getIntent().getSerializableExtra(DATE_EXTRA);
        host = (UserData) getIntent().getSerializableExtra(HOST_EXTRA);
        schedule = (Schedule) getIntent().getSerializableExtra(SCHEDULE_EXTRA);
        appointmentMap = generateAppointments(schedule);

        ((TextView) findViewById(R.id.appointmentDateText)).setText(date.toString());
        ((TextView) findViewById(R.id.appointmentHostText)).setText(host.fullName());

        appointmentsListView = findViewById(R.id.appointmentsView);
        appointmentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Appointment appointment = (Appointment) parent.getItemAtPosition(position);
                appointmentClickHandler(appointment);
            }
        });

        requestAppointments();
        Log.d("MY_CUSTOM_LOG", schedule.toString());
    }

    private HashMap<LocalTime, Appointment> generateAppointments(Schedule schedule) {
        HashMap<LocalTime, Appointment> appointments = new HashMap<>();
        Period duration = schedule.appointmentDuration;
        for (AppointmentInterval i: schedule.appointmentIntervals) {
            for (LocalTime curr = i.start; curr.isBefore(i.end); curr = curr.plus(duration)) {
                appointments.put(curr, Appointment.Empty(schedule.id, curr, curr.plus(duration)));
            }
        }
        return appointments;
    }

    private void requestAppointments() {
        ApiHttpClient.instance().getAppointments(schedule.id, new ResponseHandler<ArrayList<Appointment>>() {
            @Override
            public void handle(ArrayList<Appointment> result) {
                updateAppointments(result);
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

        ApiHttpClient.instance().createAppointment(req, new ResponseHandler<ArrayList<Appointment>>() {
            @Override
            public void handle(ArrayList<Appointment> result) {
                updateAppointments(result);
            }
        });
    }

    public void completeAppointment(Appointment selected) {

        ApiHttpClient.instance().completeAppointment(selected.id, new ResponseHandler<ArrayList<Appointment>>() {
            @Override
            public void handle(ArrayList<Appointment> result) {
                updateAppointments(result);
            }
        });
    }

    public void cancelAppointment(Appointment selected) {

        ApiHttpClient.instance().cancelAppointment(selected.id, new ResponseHandler<ArrayList<Appointment>>() {
            @Override
            public void handle(ArrayList<Appointment> result) {
                updateAppointments(result);
            }
        });
    }

    private void appointmentClickHandler(Appointment appointment) {
        appointmentsListView.setEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.appointment_item_popup,null);
        setPopupButtons(popupView, appointment);

        popupWindow = new PopupWindow(popupView, 600, LayoutParams.WRAP_CONTENT);

        ((TextView) popupView.findViewById(R.id.appointmentPopupCaption)).setText(appointment.timeInterval());

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                appointmentsListView.setEnabled(true);
                popupWindow = null;
            }
        });

        popupWindow.showAtLocation(appointmentsListView, Gravity.CENTER,0,0);
    }

    private void setPopupButtons(View popupView, final Appointment appointment) {
        Button mainButton = popupView.findViewById(R.id.appointmentPopupMainButton);

        boolean userIsHost = host.id.equals(QueueApp.getUser().id);
        boolean appointmentTaken = appointment.visitorId != null;
        boolean userHasAppointment = false;
        for (Appointment a: appointmentMap.values()) {
            if ((a.visitorId != null) && a.visitorId.equals(QueueApp.getUser().id)) {
                userHasAppointment = true;
                break;
            }
        }

        if (!appointmentTaken && !userIsHost && !userHasAppointment) {
            mainButton.setText(R.string.create_appointment);
            mainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createAppointment(appointment);
                    popupWindow.dismiss();
                }
            });
        } else if (appointmentTaken && (userIsHost || appointment.visitorId.equals(QueueApp.getUser().id))) {
            mainButton.setText(R.string.cancel_appointment);
            mainButton.setEnabled(!appointment.visited);
            mainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelAppointment(appointment);
                    popupWindow.dismiss();
                }
            });
            Button completeButton = popupView.findViewById(R.id.appointmentPopupCompleteButton);
            completeButton.setVisibility(View.VISIBLE);
            completeButton.setEnabled(!appointment.visited);
            completeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    completeAppointment(appointment);
                    popupWindow.dismiss();
                }
            });
        } else {
            mainButton.setText(R.string.create_appointment);
            mainButton.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    private void updateAppointments(ArrayList<Appointment> update) {
        appointmentMap = generateAppointments(schedule);
        for (Appointment appointment: update) {
            appointmentMap.put(appointment.start, appointment);
        }
        AppointmentItemAdapter adapter = new AppointmentItemAdapter(getApplicationContext(), appointmentMap);
        appointmentsListView.setAdapter(adapter);
    }
}
