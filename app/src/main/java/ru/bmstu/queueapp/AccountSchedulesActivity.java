package ru.bmstu.queueapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import ru.bmstu.queueapp.adapters.AccountScheduleItemAdapter;
import ru.bmstu.queueapp.http.ApiHttpClient;
import ru.bmstu.queueapp.http.ResponseHandler;
import ru.bmstu.queueapp.http.data.SchedulesData;

public class AccountSchedulesActivity extends AppCompatActivity {

    private ListView accountSchedulesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_schedules);

        accountSchedulesListView = findViewById(R.id.accountSchedulesView);

        ApiHttpClient.instance().getScheduleData(QueueApp.getUser().id, new ResponseHandler<SchedulesData>() {
            @Override
            public void handle(SchedulesData result) {
                AccountScheduleItemAdapter adapter = new AccountScheduleItemAdapter(getBaseContext(), result.schedules);
                accountSchedulesListView.setAdapter(adapter);
            }
        });
    }
}
