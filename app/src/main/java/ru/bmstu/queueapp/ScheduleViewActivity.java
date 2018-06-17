package ru.bmstu.queueapp;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import org.joda.time.Duration;
import org.joda.time.LocalDate;

import mobi.upod.timedurationpicker.TimeDurationPicker;
import mobi.upod.timedurationpicker.TimeDurationPickerDialog;
import ru.bmstu.queueapp.http.data.GenericSchedule;

public class ScheduleViewActivity extends AppCompatActivity {

    public static final String SCHEDULE_EXTRA = "genericSchedule";

    private EditText dateField;
    private EditText durationField;

    private GenericSchedule genericSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_view);

        genericSchedule = (GenericSchedule) getIntent().getSerializableExtra(SCHEDULE_EXTRA);

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
//        dpd.updateDate(); //todo preset year/month/day
        dpd.getDatePicker().setMinDate(minDate.toDate().getTime());
        dpd.show();
    }

    public void durationFieldClickHandler(View v) {

        final long minimalDuration = 300000; //5 minutes

        TimeDurationPickerDialog tdpd = new TimeDurationPickerDialog(this,
            new TimeDurationPickerDialog.OnDurationSetListener() {
                @Override
                public void onDurationSet(TimeDurationPicker view, long duration) {
                    durationField.setText(Duration.millis(duration).toPeriod().normalizedStandard().toString());
                }
            },
            0
        );
        TimeDurationPicker picker = tdpd.getDurationInput();
        picker.setTimeUnits(TimeDurationPicker.HH_MM);
        picker.setOnDurationChangeListener(new TimeDurationPicker.OnDurationChangedListener() {
            @Override
            public void onDurationChanged(TimeDurationPicker view, long duration) {
                long millisInDay = 86400000;
                if (duration > millisInDay) {
                    view.setDuration(millisInDay);
                } else if (duration < minimalDuration) {
                    view.setDuration(minimalDuration);
                }
            }
        });
        picker.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        tdpd.show();
    }
}
