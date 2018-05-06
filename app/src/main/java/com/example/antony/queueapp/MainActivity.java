package com.example.antony.queueapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.antony.queueapp.http.ApiHttpClient;
import com.example.antony.queueapp.util.UnexpectedErrorHandler;
import com.example.antony.queueapp.http.data.ScheduleDatesData;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

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

    private ArrayList<LocalDate> extractDates(JSONArray array) throws JSONException {
        ArrayList<LocalDate> dates = new ArrayList<>();
        for (int i = 0; i < array.length(); ++i) {
            dates.add(LocalDate.parse(array.get(i).toString()));
        }
        return dates;
    }

    public void scheduleButtonHandler(View v) {
        Log.d("MY_CUSTOM_LOG", "started");

        HashMap<String, String> params = new HashMap<>();
        params.put("hostId", hostInput.getText().toString());

        ApiHttpClient.get("/schedule/dates/period", new RequestParams(params), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    Period period = new Period(response.getLong("period"));
                    ArrayList<LocalDate> defaultDates = extractDates(response.getJSONArray("default"));
                    ArrayList<LocalDate> customDates = extractDates(response.getJSONArray("custom"));;

                    Intent intent = new Intent(getBaseContext(), CalendarActivity.class);
                    intent.putExtra(CalendarActivity.SCHEDULE_DATES_EXTRA, new ScheduleDatesData(period, defaultDates, customDates));
                    startActivity(intent);

                } catch (Exception e) {
                    UnexpectedErrorHandler.handle(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                UnexpectedErrorHandler.handle(e);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                UnexpectedErrorHandler.handle(e);
            }
        });
    }
}
