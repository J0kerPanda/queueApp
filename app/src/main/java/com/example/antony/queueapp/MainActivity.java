package com.example.antony.queueapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.antony.queueapp.http.ApiHttpClient;
import com.example.antony.queueapp.http.ResponseHandler;
import com.example.antony.queueapp.http.data.ScheduleDatesData;

public class MainActivity extends AppCompatActivity {

    private Button scheduleButton;
    private EditText hostInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scheduleButton = findViewById(R.id.scheduleButton);
        hostInput = findViewById(R.id.hostIdInput);

        hostInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                scheduleButton.setEnabled(s.length() > 0);
            }
        });
    }

    public void scheduleButtonHandler(View v) {
        ApiHttpClient.getScheduleDates(hostInput.getText().toString(), new ResponseHandler<ScheduleDatesData>() {
            @Override
            public void handle(ScheduleDatesData result) {
                Intent intent = new Intent(getBaseContext(), CalendarActivity.class);
                intent.putExtra(CalendarActivity.SCHEDULE_DATES_EXTRA, result);
                startActivity(intent);
            }
        });
    }
}
