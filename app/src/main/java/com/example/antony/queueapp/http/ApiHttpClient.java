package com.example.antony.queueapp.http;

import android.util.Log;

import com.example.antony.queueapp.http.data.ScheduleDatesData;
import com.example.antony.queueapp.util.UnexpectedErrorHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class ApiHttpClient {
    private static final String BASE_URL = "http://192.168.1.5:9000/api";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        Log.d("MY_CUSTOM_LOG", BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }

    public static void getScheduleDates(String hostId, final ResponseHandler<ScheduleDatesData> handler) {

        HashMap<String, String> params = new HashMap<>();
        params.put("hostId", hostId);

        ApiHttpClient.get("/schedule/dates/period", new RequestParams(params), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    Period period = new Period(response.getLong("period"));
                    ArrayList<LocalDate> defaultDates = extractDates(response.getJSONArray("default"));
                    ArrayList<LocalDate> customDates = extractDates(response.getJSONArray("custom"));
                    handler.handle(new ScheduleDatesData(period, defaultDates, customDates));

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

    private static ArrayList<LocalDate> extractDates(@NotNull JSONArray array) throws JSONException {
        ArrayList<LocalDate> dates = new ArrayList<>();
        for (int i = 0; i < array.length(); ++i) {
            dates.add(LocalDate.parse(array.get(i).toString()));
        }
        return dates;
    }
}