package com.example.antony.queueapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.antony.queueapp.http.data.ScheduleDatesData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    public static final String SCHEDULE_DATES_EXTRA = "SCHEDULE_DATES";
    public static final String HOST_ID_EXTRA = "HOST_ID";

    private MaterialCalendarView calendarView;
    private ScheduleDatesData datesData;
    private Integer hostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                widget.setDateSelected(date, !selected);
                if (!selected) {
                   Log.d("MY_CUSTOM_LOG", date.toString());
                }
            }
        });

        hostId = getIntent().getIntExtra(HOST_ID_EXTRA, -1);
        updateCalendar((ScheduleDatesData) getIntent().getSerializableExtra(SCHEDULE_DATES_EXTRA));
    }

    private void updateCalendar(ScheduleDatesData data) {
        datesData = data;
        Log.d("MY_CUSTOM_LOG", datesData.defaultDates.toString());
        Log.d("MY_CUSTOM_LOG", datesData.customDates.toString());
        LocalDate today = new LocalDate();

        calendarView.clearSelection();
        calendarView.state()
                .edit()
                .setMinimumDate(today.toDate())
                .setMaximumDate(today.plus(datesData.period.toStandardDays()).toDate())
                .commit();

        selectDates(datesData.defaultDates);
        selectDates(datesData.customDates);
    }

    private void selectDates(ArrayList<LocalDate> dates) {
        for (int i = 0; i < dates.size(); ++i) {
            calendarView.setDateSelected(dates.get(i).toDate(), true);
        }
    }
}
