package ru.bmstu.queueapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import org.jetbrains.annotations.Nullable;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;

import mobi.upod.timedurationpicker.TimeDurationPicker;
import mobi.upod.timedurationpicker.TimeDurationPickerDialog;
import ru.bmstu.queueapp.adapters.AppointmentIntervalItemAdapter;
import ru.bmstu.queueapp.http.data.AppointmentInterval;
import ru.bmstu.queueapp.http.data.Schedule;

public class ScheduleViewActivity extends AppCompatActivity {

    public static final String SCHEDULE_EXTRA = "genericSchedule";

    private EditText dateField;
    private EditText durationField;
    private EditText placeField;
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
            schedule.appointmentIntervals = new ArrayList<>();
        }

        setupForm();
    }

    private void setupForm() {
        dateField = findViewById(R.id.scheduleViewDate);
        dateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dateFieldClickHandler(v);
                    hideKeyboard(v);
                }
            }
        });

        durationField = findViewById(R.id.scheduleViewDuration);
        durationField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    durationFieldClickHandler(v);
                    hideKeyboard(v);
                }
            }
        });

        placeField = findViewById(R.id.scheduleViewPlace);

        appointmentIntervalsListView = findViewById(R.id.scheduleViewAppointmentIntervals);
        AppointmentIntervalItemAdapter adapter = new AppointmentIntervalItemAdapter(getApplicationContext(), schedule.appointmentIntervals);
        appointmentIntervalsListView.setAdapter(adapter);
        appointmentIntervalsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppointmentInterval interval = (AppointmentInterval) parent.getItemAtPosition(position);
                createAppointmentIntervalPopup(interval);
                hideKeyboard(view);
            }
        });
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

    private void createAppointmentIntervalPopup(@Nullable final AppointmentInterval interval) {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.appointment_interval_item_popup,null);

        popupWindow = new PopupWindow(popupView, 600, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupView.setElevation(5.0f);

        //todo preset by interval -> button text
        Button intervalButton = popupView.findViewById(R.id.appointmentIntervalPopupAddButton);
        setIntervalButton(popupView, intervalButton, interval);

        if (interval != null) {
            Button removeButton = popupView.findViewById(R.id.appointmentIntervalPopupRemoveButton);
            removeButton.setVisibility(View.VISIBLE);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    schedule.appointmentIntervals.remove(interval);
                    AppointmentIntervalItemAdapter adapter = new AppointmentIntervalItemAdapter(getApplicationContext(), schedule.appointmentIntervals);
                    appointmentIntervalsListView.setAdapter(adapter);
                    popupWindow.dismiss();
                }
            });
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setFieldsEnabled(true);
                popupWindow = null;
            }
        });

        setFieldsEnabled(false);
        popupWindow.showAtLocation(appointmentIntervalsListView, Gravity.CENTER,0,0);
    }

    public void setIntervalButton(View popupView, final Button button, @Nullable final AppointmentInterval interval) {
        button.setEnabled(false);
        final EditText startText = popupView.findViewById(R.id.appointmentIntervalPopupStart);
        setTimeEditText(startText);
        final EditText endText = popupView.findViewById(R.id.appointmentIntervalPopupEnd);
        setTimeEditText(endText);
        if (interval != null) {
            startText.setText(interval.start.toString("HH:mm"));
            endText.setText(interval.end.toString("HH:mm"));
        }

        startText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                button.setEnabled((count > 0) && (endText.length() > 0));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        endText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                button.setEnabled((count > 0) && (startText.length() > 0));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final AppointmentInterval result = interval == null ? new AppointmentInterval() : interval;

        if (interval != null) {
            button.setText(R.string.appointment_interval_popup_update_button);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalTime start = LocalTime.parse(startText.getText().toString());
                LocalTime end = LocalTime.parse(endText.getText().toString());

                if (start.isBefore(end)) {
                    result.start = start;
                    result.end = end;
                } else {
                    result.start = end;
                    result.end = start;
                }

                if (interval == null) {
                    schedule.appointmentIntervals.add(result);
                }

                AppointmentIntervalItemAdapter adapter = new AppointmentIntervalItemAdapter(getApplicationContext(), schedule.appointmentIntervals);
                appointmentIntervalsListView.setAdapter(adapter);

                popupWindow.dismiss();
            }
        });
    }

    private void timeEditTextClickHandler(final EditText text) {
        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        text.setText(new LocalTime(hourOfDay, minute).toString("HH:mm"));
                    }
                },
                0, 0,true
        );

        tpd.show();
    }

    private void setTimeEditText(final EditText text) {
        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    timeEditTextClickHandler(text);
                }
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeEditTextClickHandler(text);
            }
        });
    }

    public void addAppointmentIntervalButtonHandler(View v) {
        hideKeyboard(v);
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

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void setFieldsEnabled(boolean enabled) {
        dateField.setEnabled(enabled);
        durationField.setEnabled(enabled);
        placeField.setEnabled(enabled);
    }
}
