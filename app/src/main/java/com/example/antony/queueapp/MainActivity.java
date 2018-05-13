package com.example.antony.queueapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.antony.queueapp.http.ApiHttpClient;
import com.example.antony.queueapp.http.ResponseHandler;
import com.example.antony.queueapp.http.data.HostData;
import com.example.antony.queueapp.http.data.ScheduleData;

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

        updateHosts();
    }

    public void scheduleButtonHandler(View v) {
        final HostData host = (HostData) hostSpinner.getSelectedItem();
        ApiHttpClient.getScheduleData(String.valueOf(host.id), new ResponseHandler<ScheduleData>() {
            @Override
            public void handle(ScheduleData result) {
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
        ApiHttpClient.getHosts(new ResponseHandler<ArrayList<HostData>>() {
            @Override
            public void handle(ArrayList<HostData> result) {
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
