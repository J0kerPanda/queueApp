package ru.bmstu.queueapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import org.jetbrains.annotations.Nullable;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import mobi.upod.timedurationpicker.TimeDurationPicker;
import mobi.upod.timedurationpicker.TimeDurationPickerDialog;
import ru.bmstu.queueapp.adapters.AppointmentIntervalItemAdapter;
import ru.bmstu.queueapp.http.data.AppointmentInterval;
import ru.bmstu.queueapp.http.data.Schedule;

public class ScheduleViewActivity extends AppCompatActivity {

    public static final String SCHEDULE_EXTRA = "genericSchedule";

    private EditText dateField;
    private EditText durationField;
    private ListView appointmentIntervalsListView;
    private PopupWindow popupWindow;

    private Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_view);

        schedule = (Schedule) getIntent().getSerializableExtra(SCHEDULE_EXTRA);
        if (schedule == null) {
            schedule = new Schedule();
        }

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

        appointmentIntervalsListView = findViewById(R.id.scheduleViewAppointmentIntervals);
        if (schedule.appointmentIntervals != null) {
            AppointmentIntervalItemAdapter adapter = new AppointmentIntervalItemAdapter(getApplicationContext(), schedule.appointmentIntervals);
            appointmentIntervalsListView.setAdapter(adapter);
        }
    }

    public void dateFieldClickHandler(View v) {
        LocalDate minDate = LocalDate.now();
        if ((schedule.date != null) && schedule.date.isBefore(minDate)) {
            minDate = schedule.date;
        }
        LocalDate selectedDate = (schedule.date != null) ? schedule.date : minDate;

        DatePickerDialog dpd = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    dateField.setText(new LocalDate(year, monthOfYear, dayOfMonth).toString());
                }
            },
            selectedDate.getYear(),
            selectedDate.getMonthOfYear() - 1,
            selectedDate.getDayOfMonth()
        );
        dpd.getDatePicker().setMinDate(minDate.toDate().getTime());
        dpd.show();
    }

    public void durationFieldClickHandler(View v) {
        final long millisInDay = 86400000;

        TimeDurationPickerDialog tdpd = new TimeDurationPickerDialog(this,
            new TimeDurationPickerDialog.OnDurationSetListener() {
                @Override
                public void onDurationSet(TimeDurationPicker view, long duration) {
                    durationField.setText(Duration.millis(duration).toPeriod().normalizedStandard().toString());
                }
            },
            (schedule.appointmentDuration != null) ? schedule.appointmentDuration.getMillis() : 0
        );
        TimeDurationPicker picker = tdpd.getDurationInput();
        picker.setTimeUnits(TimeDurationPicker.HH_MM);
        picker.setOnDurationChangeListener(new TimeDurationPicker.OnDurationChangedListener() {
            @Override
            public void onDurationChanged(TimeDurationPicker view, long duration) {
                if (duration > millisInDay) {
                    view.setDuration(millisInDay);
                }
            }
        });

        tdpd.show();
    }

    private void createAppointmentIntervalPopup(@Nullable AppointmentInterval interval) {
        appointmentIntervalsListView.setEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.appointment_interval_item_popup,null);

        popupWindow = new PopupWindow(popupView, 600, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupView.setElevation(5.0f);

        //todo preset by interval -> button text
        Button intervalButton = popupView.findViewById(R.id.appointmentIntervalPopupButton);
        setIntervalButton(popupView, intervalButton, interval);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                appointmentIntervalsListView.setEnabled(true);
                popupWindow = null;
            }
        });

        popupWindow.showAtLocation(appointmentIntervalsListView, Gravity.CENTER,0,0);
    }

    public void setIntervalButton(View popupView, Button button, @Nullable AppointmentInterval interval) {
        EditText startText = popupView.findViewById(R.id.appointmentIntervalPopupStart);
        setTimeEditText(startText);
        EditText endText = popupView.findViewById(R.id.appointmentIntervalPopupEnd);
        setTimeEditText(endText);

        if (interval == null) {
            button.setText(R.string.appointment_popup_update_button);

        }
    }

    private void setTimeEditText(final EditText text) {
        TimePickerDialog tpd = new TimePickerDialog(this,
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    text.setText(new LocalTime(hourOfDay, minute).toString());
                }
            },
            0, 0,false
        );

        tpd.show();
    }

    public void addAppointmentIntervalButtonHandler(View v) {
        createAppointmentIntervalPopup(null);
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
