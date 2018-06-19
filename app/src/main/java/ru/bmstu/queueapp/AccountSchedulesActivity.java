package ru.bmstu.queueapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.joda.time.LocalDate;

import java.util.Map;

import ru.bmstu.queueapp.adapters.AccountScheduleItemAdapter;
import ru.bmstu.queueapp.http.ApiHttpClient;
import ru.bmstu.queueapp.http.ResponseHandler;
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
        accountSchedulesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map.Entry<LocalDate, Schedule> el = (Map.Entry<LocalDate, Schedule>) parent.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), ScheduleViewActivity.class);
                Log.d("MY_CUSTOM_LOG", el.getKey().toString());
                Log.d("MY_CUSTOM_LOG", el.getValue().toString());
                el.getValue().date = el.getKey();
                intent.putExtra(ScheduleViewActivity.SCHEDULE_EXTRA, el.getValue());
                startActivityForResult(intent, SCHEDULE_VIEW);
            }
        });

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
