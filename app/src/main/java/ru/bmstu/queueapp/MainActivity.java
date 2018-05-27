package ru.bmstu.queueapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import ru.bmstu.queueapp.http.ApiHttpClient;
import ru.bmstu.queueapp.http.ResponseHandler;
import ru.bmstu.queueapp.http.data.LoginData;
import ru.bmstu.queueapp.http.data.UserData;
import ru.bmstu.queueapp.http.data.SchedulesData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner hostSpinner;
    private Button scheduleButton;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hostSpinner = findViewById(R.id.hostSpinner);
        scheduleButton = findViewById(R.id.scheduleButton);
        refreshButton = findViewById(R.id.refreshButton);

        hostSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                scheduleButton.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                scheduleButton.setEnabled(false);
            }
        });

        ApiHttpClient.instance().login(new LoginData("test1@email.com", "test"));
        updateHosts();
    }

    public void scheduleButtonHandler(View v) {
        final UserData host = (UserData) hostSpinner.getSelectedItem();
        ApiHttpClient.instance().getScheduleData(String.valueOf(host.id), new ResponseHandler<SchedulesData>() {
            @Override
            public void handle(SchedulesData result) {
                Intent intent = new Intent(getBaseContext(), CalendarActivity.class);
                intent.putExtra(CalendarActivity.SCHEDULE_DATA_EXTRA, result);
                intent.putExtra(CalendarActivity.HOST_EXTRA, host);
                startActivity(intent);
            }
        });
    }

    public void refreshButtonHandler(View v) {
        updateHosts();
    }

    private void updateHosts() {
        refreshButton.setEnabled(false);
        ApiHttpClient.instance().getHosts(new ResponseHandler<ArrayList<UserData>>() {
            @Override
            public void handle(ArrayList<UserData> result) {
                Log.d("MY_CUSTOM_LOG", result.toString());
                hostSpinner.setAdapter(new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.spinner_item,
                    result
                ));
                refreshButton.setEnabled(true);
            }
        });
    }
}
