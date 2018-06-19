package ru.bmstu.queueapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import ru.bmstu.queueapp.adapters.AccountScheduleItemAdapter;
import ru.bmstu.queueapp.http.ApiHttpClient;
import ru.bmstu.queueapp.http.ResponseHandler;
import ru.bmstu.queueapp.http.data.GenericSchedule;
import ru.bmstu.queueapp.http.data.Schedule;
import ru.bmstu.queueapp.http.data.SchedulesData;

public class AccountSchedulesActivity extends AppCompatActivity {

    private ListView accountSchedulesListView;

    public static final String SCHEDULE_EXTRA = "SCHEDULE_DATA";

    public static final int SCHEDULE_VIEW = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_schedules);

        accountSchedulesListView = findViewById(R.id.accountSchedulesView);

        ApiHttpClient.instance().getScheduleData(QueueApp.getUser().id, new ResponseHandler<SchedulesData>() {
            @Override
            public void handle(SchedulesData result) {
                updateSchedules(result);
            }
        });
    }

    public void createScheduleTransitionButtonHandler(View v) {
        Intent intent = new Intent(getBaseContext(), ScheduleViewActivity.class);
        startActivityForResult(intent, SCHEDULE_VIEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == SCHEDULE_VIEW) && (resultCode == RESULT_OK)) {
            SchedulesData schedules = (SchedulesData) data.getSerializableExtra(SCHEDULE_EXTRA);
            updateSchedules(schedules);
        }
    }

    private void updateSchedules(SchedulesData data) {
        AccountScheduleItemAdapter adapter = new AccountScheduleItemAdapter(getBaseContext(), data.schedules);
        accountSchedulesListView.setAdapter(adapter);
    }
}
