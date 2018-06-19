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

import ru.bmstu.queueapp.adapters.AccountScheduleItemAdapter;
import ru.bmstu.queueapp.http.ApiHttpClient;
import ru.bmstu.queueapp.http.ResponseHandler;
import ru.bmstu.queueapp.http.data.Schedule;
import ru.bmstu.queueapp.http.data.SchedulesData;

public class AccountSchedulesActivity extends AppCompatActivity {

    private ListView accountSchedulesListView;

    public static final String SCHEDULE_DATA_EXTRA = "SCHEDULE_DATA";

    public static final int SCHEDULE_VIEW = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_schedules);

        accountSchedulesListView = findViewById(R.id.accountSchedulesView);
        accountSchedulesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map.Entry<LocalDate, Schedule> el = (Map.Entry<LocalDate, Schedule>) parent.getItemAtPosition(position);
                el.getValue().date = el.getKey();
                transitionToScheduleView(el.getValue());
            }
        });

        ApiHttpClient.instance().getScheduleData(QueueApp.getUser().id, new ResponseHandler<SchedulesData>() {
            @Override
            public void handle(SchedulesData result) {
                updateSchedules(result);
            }
        });
    }

    public void transitionToScheduleView(@Nullable Schedule schedule) {
        Intent intent = new Intent(getBaseContext(), ScheduleViewActivity.class);
        intent.putExtra(ScheduleViewActivity.SCHEDULE_EXTRA, schedule);

        int dateCount = accountSchedulesListView.getAdapter().getCount();
        ArrayList<Calendar> excluded = new ArrayList<>();
        for (int i = 0; i < dateCount; ++i) {
            LocalDate date = ((Map.Entry<LocalDate, Schedule>) accountSchedulesListView.getItemAtPosition(i)).getKey();
            if ((schedule == null) || !schedule.date.isEqual(date)) {
                excluded.add(date.plusDays(1).toDateTimeAtStartOfDay().toCalendar(null));
            }
        }
        intent.putExtra(ScheduleViewActivity.EXCLUDED_DATES, excluded.toArray(new Calendar[excluded.size()]));
        startActivityForResult(intent, SCHEDULE_VIEW);
    }

    public void createScheduleTransitionButtonHandler(View v) {
        transitionToScheduleView(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == SCHEDULE_VIEW) && (resultCode == RESULT_OK)) {
            SchedulesData schedules = (SchedulesData) data.getSerializableExtra(SCHEDULE_DATA_EXTRA);
            updateSchedules(schedules);
        }
    }

    private void updateSchedules(SchedulesData data) {
        AccountScheduleItemAdapter adapter = new AccountScheduleItemAdapter(getBaseContext(), data.schedules);
        accountSchedulesListView.setAdapter(adapter);
    }
}
