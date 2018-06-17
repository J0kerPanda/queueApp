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
import ru.bmstu.queueapp.http.data.GenericSchedule;
import ru.bmstu.queueapp.http.data.UserData;
import ru.bmstu.queueapp.http.request.CreateAppointmentRequest;

public class AppointmentsActivity extends AppCompatActivity {

    public static final String DATE_EXTRA = "DATE";
    public static final String HOST_EXTRA = "HOST";
    public static final String SCHEDULE_EXTRA = "SCHEDULE";

    private ListView appointmentsListView;

    private LocalDate date;
    private UserData host;
    private GenericSchedule genericSchedule;
    private HashMap<LocalTime, Appointment> appointmentMap;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        date = (LocalDate) getIntent().getSerializableExtra(DATE_EXTRA);
        host = (UserData) getIntent().getSerializableExtra(HOST_EXTRA);
        genericSchedule = (GenericSchedule) getIntent().getSerializableExtra(SCHEDULE_EXTRA);
        appointmentMap = generateAppointments(genericSchedule);

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
        Log.d("MY_CUSTOM_LOG", genericSchedule.toString());
    }

    private HashMap<LocalTime, Appointment> generateAppointments(GenericSchedule genericSchedule) {
        HashMap<LocalTime, Appointment> appointments = new HashMap<>();
        Period duration = genericSchedule.appointmentDuration;
        for (AppointmentInterval i: genericSchedule.appointmentIntervals) {
            for (LocalTime curr = i.start; curr.isBefore(i.end); curr = curr.plus(duration)) {
                appointments.put(curr, Appointment.Empty(genericSchedule.id, curr, curr.plus(duration)));
            }
        }
        return appointments;
    }

    private void requestAppointments() {
        ApiHttpClient.instance().getAppointments(genericSchedule.id, new ResponseHandler<ArrayList<Appointment>>() {
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
                    reloadActivity();
                }
            }
        });
    }

    public void cancelAppointment(Appointment selected) {

        ApiHttpClient.instance().cancelAppointment(selected.id, new ResponseHandler<Boolean>() {
            @Override
            public void handle(Boolean result) {
                //todo false case
                if (result) {
                    reloadActivity();
                }
            }
        });
    }

    private void appointmentClickHandler(Appointment appointment) {
        appointmentsListView.setEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.appointment_item_popup,null);

        popupWindow = new PopupWindow(popupView, 600, LayoutParams.WRAP_CONTENT);

        ((TextView) popupView.findViewById(R.id.appointmentPopupCaption)).setText(appointment.timeInterval());
        Button appointmentButton = popupView.findViewById(R.id.appointmentPopupButton);
        setPopupButton(appointmentButton, appointment);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                appointmentsListView.setEnabled(true);
                popupWindow = null;
            }
        });

        popupWindow.showAtLocation(appointmentsListView, Gravity.CENTER,0,0);
    }

    private void setPopupButton(Button button, final Appointment appointment) {
         if ((appointment.visitorId == null) && !host.id.equals(QueueApp.getUser().id)) {
            button.setText(R.string.create_appointment);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo check if not taken
                    createAppointment(appointment);
                }
            });
        } else if (host.id.equals(QueueApp.getUser().id) || appointment.visitorId.equals(QueueApp.getUser().id)) {
            button.setText(R.string.cancel_appointment);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelAppointment(appointment);
                }
            });
        } else {
            button.setText(R.string.create_appointment);
            button.setEnabled(false);
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

    private void reloadActivity() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        finish();
        startActivity(getIntent());
        //todo put sorted stuff in intent?
        //todo plain add without refresh -> confirmation?
        overridePendingTransition(0, 0);
    }
}
