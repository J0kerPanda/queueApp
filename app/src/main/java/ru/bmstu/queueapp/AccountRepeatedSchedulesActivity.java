package ru.bmstu.queueapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import ru.bmstu.queueapp.adapters.AccountRepeatedScheduleItemAdapter;
import ru.bmstu.queueapp.http.data.RepeatedSchedule;
import ru.bmstu.queueapp.http.data.RepeatedSchedulesData;

public class AccountRepeatedSchedulesActivity extends AppCompatActivity {

    private ListView accountRepeatedSchedulesListView;

    public static final String SCHEDULE_DATA_EXTRA = "SCHEDULE_DATA";

    public static final int SCHEDULE_VIEW = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_repeated_schedules);

        accountRepeatedSchedulesListView = findViewById(R.id.accountRepeatedSchedulesView);
        accountRepeatedSchedulesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map.Entry<LocalDate, RepeatedSchedule> el = (Map.Entry<LocalDate, RepeatedSchedule>) parent.getItemAtPosition(position);
                el.getValue().repeatDate = el.getKey();
                transitionToScheduleView(el.getValue());
            }
        });

        //todo get schedules -> forbidden dates
        // todo get repeated -> display
    }

    public void transitionToScheduleView(@Nullable RepeatedSchedule schedule) {
        Intent intent = new Intent(getBaseContext(), RepeatedScheduleViewActivity.class);
        intent.putExtra(RepeatedScheduleViewActivity.SCHEDULE_EXTRA, schedule);

        int dateCount = accountRepeatedSchedulesListView.getAdapter().getCount();
        ArrayList<Calendar> excluded = new ArrayList<>();
        for (int i = 0; i < dateCount; ++i) {
            LocalDate date = ((Map.Entry<LocalDate, RepeatedSchedule>) accountRepeatedSchedulesListView.getItemAtPosition(i)).getKey();
            if ((schedule == null) || !schedule.repeatDate.isEqual(date)) {
                excluded.add(date.plusDays(1).toDateTimeAtStartOfDay().toCalendar(null));
            }
        }
        intent.putExtra(RepeatedScheduleViewActivity.EXCLUDED_DATES, excluded.toArray(new Calendar[excluded.size()]));
        startActivityForResult(intent, SCHEDULE_VIEW);
    }

    public void createScheduleTransitionButtonHandler(View v) {
        transitionToScheduleView(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == SCHEDULE_VIEW) && (resultCode == RESULT_OK)) {
            RepeatedSchedulesData schedules = (RepeatedSchedulesData) data.getSerializableExtra(SCHEDULE_DATA_EXTRA);
            updateSchedules(schedules);
        }
    }

    private void updateSchedules(RepeatedSchedulesData data) {
        AccountRepeatedScheduleItemAdapter adapter = new AccountRepeatedScheduleItemAdapter(getBaseContext(), data.schedules);
        accountRepeatedSchedulesListView.setAdapter(adapter);
    }
}
