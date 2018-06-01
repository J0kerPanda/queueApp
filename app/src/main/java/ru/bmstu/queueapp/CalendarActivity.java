package ru.bmstu.queueapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import ru.bmstu.queueapp.http.ApiHttpClient;
import ru.bmstu.queueapp.http.ResponseHandler;
import ru.bmstu.queueapp.http.data.Schedule;
import ru.bmstu.queueapp.http.data.SchedulesData;
import ru.bmstu.queueapp.http.data.UserData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CalendarActivity extends AppCompatActivity {

    public static final String SCHEDULE_DATA_EXTRA = "SCHEDULE_DATES";
    public static final String HOST_EXTRA = "HOST";

    private Spinner hostSpinner;
    private MaterialCalendarView calendarView;
    private Button refreshButton;

    private SchedulesData schedulesData;
    private UserData host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        hostSpinner = findViewById(R.id.calendarHostSpinner);
        hostSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                host = (UserData) parent.getItemAtPosition(position);
                //todo cache?
                ApiHttpClient.instance().getScheduleData(String.valueOf(host.id), new ResponseHandler<SchedulesData>() {
                    @Override
                    public void handle(SchedulesData result) {
                        updateCalendar(result);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                host = null;
            }
        });

        refreshButton = findViewById(R.id.calendarRefreshButton);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                widget.setDateSelected(date, !selected);
                LocalDate localDate = new LocalDate(date.getDate());
                if (!selected && (host != null)) {
                    Log.d("MY_CUSTOM_LOG", date.toString());
                    Intent intent = new Intent(getBaseContext(), AppointmentsActivity.class);

                    ArrayList<Schedule> dateSchedules = new ArrayList<>();
                    for (Schedule schedule: schedulesData.schedules) {
                        if (schedule.date.isEqual(localDate)) {
                            dateSchedules.add(schedule);
                        }
                    }

                    intent.putExtra(AppointmentsActivity.DATE_EXTRA, localDate);
                    intent.putExtra(AppointmentsActivity.HOST_EXTRA, host);
                    intent.putExtra(AppointmentsActivity.SCHEDULES_EXTRA, dateSchedules);
                    startActivity(intent);
                }
            }
        });

        updateHosts();
    }

    private void updateCalendar(SchedulesData data) {
        schedulesData = data;
        Log.d("MY_CUSTOM_LOG", schedulesData.schedules.toString());
        LocalDate today = new LocalDate();

        calendarView.clearSelection();
        calendarView.state()
                .edit()
                .setMinimumDate(today.toDate())
                .setMaximumDate(today.plus(schedulesData.period.toStandardDays()).toDate())
                .commit();

        for (Schedule schedule: schedulesData.schedules) {
            calendarView.setDateSelected(schedule.date.toDate(), true);
        }
    }

    public void refreshButtonHandler(View v) {
        updateHosts();
    }

    public void logoutButtonHandler(View v) {
        QueueApp.removeUser();
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void updateHosts() {
        host = null;
        refreshButton.setEnabled(false);
        ApiHttpClient.instance().getHosts(new ResponseHandler<ArrayList<UserData>>() {
            @Override
            public void handle(ArrayList<UserData> result) {
                Collections.sort(result, new Comparator<UserData>() {
                    @Override
                    public int compare(UserData o1, UserData o2) {
                        return o1.surname.compareTo(o2.surname);
                    }
                });
                Log.d("MY_CUSTOM_LOG", result.toString());
                hostSpinner.setAdapter(new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.spinner_item,
                    result
                ));
                refreshButton.setEnabled(true);
            }
        });
    }
}
