package com.example.antony.queueapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.antony.queueapp.http.data.ScheduleData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;

import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {

    public static final String SCHEDULE_DATA_EXTRA = "SCHEDULE_DATES";
    public static final String HOST_ID_EXTRA = "HOST_ID";

    private MaterialCalendarView calendarView;
    private ScheduleData scheduleData;
    private Integer hostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);

        //todo fetch schedules for dates

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                widget.setDateSelected(date, !selected);
                LocalDate localDate = new LocalDate(date.getDate());
                if (!selected) {
                   Log.d("MY_CUSTOM_LOG", date.toString());
                   Intent intent = new Intent(getBaseContext(), DateAppointmentsActivity.class);

                   intent.putExtra(DateAppointmentsActivity.DATE_EXTRA, localDate);
                   intent.putExtra(DateAppointmentsActivity.HOST_ID_EXTRA, hostId);
                   startActivity(intent);
                }
            }
        });

        hostId = getIntent().getIntExtra(HOST_ID_EXTRA, -1);
        updateCalendar((ScheduleData) getIntent().getSerializableExtra(SCHEDULE_DATA_EXTRA));
    }

    private void updateCalendar(ScheduleData data) {
        scheduleData = data;
        Log.d("MY_CUSTOM_LOG", scheduleData.schedules.toString());
        LocalDate today = new LocalDate();

        calendarView.clearSelection();
        calendarView.state()
                .edit()
                .setMinimumDate(today.toDate())
                .setMaximumDate(today.plus(scheduleData.period.toStandardDays()).toDate())
                .commit();

        for (int i =0; i < scheduleData.schedules.size(); ++i ) {
            calendarView.setDateSelected(scheduleData.schedules.get(i).date.toDate(), true);
        }
    }

    private void selectDates(@NotNull ArrayList<LocalDate> dates) {
        for (int i = 0; i < dates.size(); ++i) {
            calendarView.setDateSelected(dates.get(i).toDate(), true);
        }
    }
}
