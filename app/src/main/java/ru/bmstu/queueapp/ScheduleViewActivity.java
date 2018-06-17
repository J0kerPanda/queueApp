package ru.bmstu.queueapp;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import mobi.upod.timedurationpicker.TimeDurationPicker;
import mobi.upod.timedurationpicker.TimeDurationPickerDialog;

public class ScheduleViewActivity extends AppCompatActivity {

    private EditText dateField;
    private EditText durationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_view);

        dateField = findViewById(R.id.scheduleViewDate);
        dateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dateFieldClickHandler(v);
                }
            }
        });
        durationField = findViewById(R.id.scheduleViewDuration);
        durationField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    durationFieldClickHandler(v);
                }
            }
        });
    }

    public void dateFieldClickHandler(View v) {

        LocalDate minDate = LocalDate.now();

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    dateField.setText(new LocalDate(year, monthOfYear, dayOfMonth).toString());
                }
            },
            minDate.getYear(),
            minDate.getMonthOfYear() - 1,
            minDate.getDayOfMonth()
        );
//        dpd.updateDate();
        //todo default year/month/day
        dpd.getDatePicker().setMinDate(minDate.toDate().getTime());
        dpd.show();
    }

    public void durationFieldClickHandler(View v) {

        TimeDurationPickerDialog dpd = new TimeDurationPickerDialog(this,
            new TimeDurationPickerDialog.OnDurationSetListener() {
                @Override
                public void onDurationSet(TimeDurationPicker view, long duration) {
                    durationField.setText(LocalTime.fromMillisOfDay(duration).toString());
                }
            },
            0
        );
        dpd.getDurationInput().setOnDurationChangeListener(new TimeDurationPicker.OnDurationChangedListener() {
            @Override
            public void onDurationChanged(TimeDurationPicker view, long duration) {
                long millisInDay = 86400000;
                if (duration > millisInDay) {
                    view.setDuration(millisInDay);
                }
            }
        });
        dpd.getDurationInput().setTimeUnits(TimeDurationPicker.HH_MM);
        dpd.show();
    }
}
