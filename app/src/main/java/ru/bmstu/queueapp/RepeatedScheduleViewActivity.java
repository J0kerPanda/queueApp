package ru.bmstu.queueapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import mobi.upod.timedurationpicker.TimeDurationPicker;
import mobi.upod.timedurationpicker.TimeDurationPickerDialog;
import ru.bmstu.queueapp.adapters.AppointmentIntervalItemAdapter;
import ru.bmstu.queueapp.http.ApiHttpClient;
import ru.bmstu.queueapp.http.ResponseHandler;
import ru.bmstu.queueapp.http.data.AppointmentInterval;
import ru.bmstu.queueapp.http.data.RepeatedSchedule;
import ru.bmstu.queueapp.http.data.RepeatedSchedulesData;

public class RepeatedScheduleViewActivity extends AppCompatActivity {

    public static final String SCHEDULE_EXTRA = "SCHEDULE";
    public static final String EXCLUDED_DATES = "EXCLUDED";

    private EditText dateField;
    private EditText repeatPeriodField;
    private EditText durationField;
    private EditText placeField;
    private Button addIntervalButton;
    private ListView appointmentIntervalsListView;
    private Button createButton;
    private Button deleteButton;
    private PopupWindow popupWindow;
    private final PeriodFormatter periodFormat = PeriodFormat.wordBased(new Locale("ru"));

    private RepeatedSchedule schedule;
    private Calendar[] excludedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeated_schedule_view);

        schedule = (RepeatedSchedule) getIntent().getSerializableExtra(SCHEDULE_EXTRA);
        if (schedule == null) {
            schedule = new RepeatedSchedule();
            schedule.appointmentIntervals = new ArrayList<>();
        }

        excludedDates = (Calendar[]) getIntent().getSerializableExtra(EXCLUDED_DATES);
        setupForm();
    }

    private void setupForm() {
        dateField = findViewById(R.id.repeatedScheduleViewDate);
        dateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dateFieldClickHandler(v);
                    hideKeyboard(v);
                }
            }
        });
        addCreateButtonTextMonitor(dateField);

        repeatPeriodField = findViewById(R.id.repeatedScheduleViewRepeatPeriod);
        repeatPeriodField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    schedule.repeatPeriod = Period.days(Integer.parseInt(s.toString()));
                }
            }
        });
        addCreateButtonTextMonitor(repeatPeriodField);

        durationField = findViewById(R.id.repeatedScheduleViewDuration);
        durationField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    durationFieldClickHandler(v);
                    hideKeyboard(v);
                }
            }
        });
        addCreateButtonTextMonitor(durationField);

        placeField = findViewById(R.id.repeatedScheduleViewPlace);
        addCreateButtonTextMonitor(placeField);
        placeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    schedule.place = s.toString();
                }
            }
        });

        addIntervalButton = findViewById(R.id.repeatedScheduleViewAddIntervalButton);
        appointmentIntervalsListView = findViewById(R.id.repeatedScheduleViewAppointmentIntervals);
        updateIntervals(schedule.appointmentIntervals);
        appointmentIntervalsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppointmentInterval interval = (AppointmentInterval) parent.getItemAtPosition(position);
                createAppointmentIntervalPopup(interval);
                hideKeyboard(view);
            }
        });

        createButton = findViewById(R.id.repeatedScheduleViewCreateButton);
        createButton.setEnabled(false);

        deleteButton = findViewById(R.id.repeatedScheduleViewDeleteButton);

        if (schedule.appointmentIntervals.size() > 0) {
            dateField.setText(schedule.repeatDate.toString());
            repeatPeriodField.setText(Integer.toString(schedule.repeatPeriod.getDays()));
            durationField.setText(periodFormat.print(schedule.appointmentDuration));
            placeField.setText(schedule.place);
            createButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.VISIBLE);
        }
    }

    public void dateFieldClickHandler(View v) {
        LocalDate minDate = LocalDate.now();
        if ((schedule.repeatDate != null) && schedule.repeatDate.isBefore(minDate)) {
            minDate = schedule.repeatDate;
        }
        LocalDate selectedDate = (schedule.repeatDate != null) ? schedule.repeatDate : minDate;

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        LocalDate date = new LocalDate(year, monthOfYear + 1, dayOfMonth);
                        dateField.setText(date.toString());
                        schedule.repeatDate = date;
                    }
                },
                selectedDate.getYear(),
                selectedDate.getMonthOfYear() - 1,
                selectedDate.getDayOfMonth()
        );

        Log.d("MY_CUSTOM_LOG", minDate.toDateTime(LocalTime.MIDNIGHT).toCalendar(null).toString());

        dpd.setMinDate(minDate.plusDays(1).toDateTimeAtStartOfDay().toCalendar(null));
        dpd.setDisabledDays(excludedDates);
        dpd.show(getFragmentManager(), "DatePickerDialog");
    }

    public void durationFieldClickHandler(View v) {
        final long millisInDay = 86400000;

        TimeDurationPickerDialog tdpd = new TimeDurationPickerDialog(this,
                new TimeDurationPickerDialog.OnDurationSetListener() {
                    @Override
                    public void onDurationSet(TimeDurationPicker view, long duration) {
                        Period appointmentDuration = new Period(duration);
                        durationField.setText(periodFormat.print(appointmentDuration));
                        schedule.appointmentDuration = appointmentDuration;
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

        //todo preset by interval -> button text
        Button intervalButton = popupView.findViewById(R.id.appointmentIntervalPopupAddButton);
        setIntervalButton(popupView, intervalButton, interval);
        intervalButton.setEnabled(false);

        if (interval != null) {
            Button removeButton = popupView.findViewById(R.id.appointmentIntervalPopupRemoveButton);
            removeButton.setVisibility(View.VISIBLE);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    schedule.appointmentIntervals.remove(interval);
                    updateIntervals(schedule.appointmentIntervals);
                    checkFields();
                    popupWindow.dismiss();
                }
            });
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setFormEnabled(true);
                popupWindow = null;
            }
        });

        setFormEnabled(false);
        popupWindow.showAtLocation(appointmentIntervalsListView, Gravity.CENTER,0,0);
    }

    public void setIntervalButton(View popupView, final Button button, @Nullable final AppointmentInterval interval) {
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
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                button.setEnabled((s.length() > 0) && (endText.length() > 0));
            }
        });

        endText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                button.setEnabled((s.length() > 0) && (startText.length() > 0));
            }
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

                updateIntervals(schedule.appointmentIntervals);
                checkFields();
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

    private void checkFields() {
        createButton.setEnabled(
            (dateField.length() > 0) &&
            (repeatPeriodField.length() > 0) &&
            (durationField.length() > 0) &&
            (placeField.length() > 0) &&
            (appointmentIntervalsListView.getAdapter().getCount() > 0)
        );
    }

    private void addCreateButtonTextMonitor(EditText text) {
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                checkFields();
            }
        });
    }

    private void updateIntervals(ArrayList<AppointmentInterval> intervals) {
        AppointmentIntervalItemAdapter adapter = new AppointmentIntervalItemAdapter(getApplicationContext(), intervals);
        appointmentIntervalsListView.setAdapter(adapter);
    }

    private void setFormEnabled(boolean enabled) {
        dateField.setEnabled(enabled);
        repeatPeriodField.setEnabled(enabled);
        durationField.setEnabled(enabled);
        placeField.setEnabled(enabled);
        addIntervalButton.setEnabled(enabled);
        appointmentIntervalsListView.setEnabled(enabled);
    }

    public void createScheduleButtonHandler(View v) {
        schedule.repeatDate = schedule.repeatDate.minus(schedule.repeatPeriod);

        ApiHttpClient.instance().createRepeatedSchedule(schedule, new ResponseHandler<RepeatedSchedulesData>() {
            @Override
            public void handle(RepeatedSchedulesData result) {
                Intent intent = new Intent();
                intent.putExtra(AccountRepeatedSchedulesActivity.SCHEDULE_DATA_EXTRA, result);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void deleteScheduleButtonHandler(View v) {

        ApiHttpClient.instance().deleteRepeatedSchedule(schedule.id, new ResponseHandler<RepeatedSchedulesData>() {
            @Override
            public void handle(RepeatedSchedulesData result) {
                Intent intent = new Intent();
                intent.putExtra(AccountSchedulesActivity.SCHEDULE_DATA_EXTRA, result);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
