package com.example.antony.queueapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.antony.queueapp.http.data.UserData;
import com.example.antony.queueapp.http.data.Schedule;
import com.example.antony.queueapp.http.data.SchedulesData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;

import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {

    public static final String SCHEDULE_DATA_EXTRA = "SCHEDULE_DATES";
    public static final String HOST_EXTRA = "HOST";

    private MaterialCalendarView calendarView;

    private SchedulesData schedulesData;
    private UserData host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);

        host = (UserData) getIntent().getSerializableExtra(HOST_EXTRA);
        updateCalendar((SchedulesData) getIntent().getSerializableExtra(SCHEDULE_DATA_EXTRA));

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                widget.setDateSelected(date, !selected);
                LocalDate localDate = new LocalDate(date.getDate());
                if (!selected) {
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

    private void selectDates(@NotNull ArrayList<LocalDate> dates) {
        for (int i = 0; i < dates.size(); ++i) {
            calendarView.setDateSelected(dates.get(i).toDate(), true);
        }
    }
}
