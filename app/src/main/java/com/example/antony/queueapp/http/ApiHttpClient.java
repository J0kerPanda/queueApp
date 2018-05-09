package com.example.antony.queueapp.http;

import android.util.Log;

import com.example.antony.queueapp.http.data.HostData;
import com.example.antony.queueapp.http.data.ScheduleData;
import com.example.antony.queueapp.util.UnexpectedErrorHandler;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ApiHttpClient {
    private static final String BASE_URL = "http://192.168.1.5:9000/api";

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static final Gson gson = new JsonExtractor().gson;

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

    public static void getScheduleDates(String hostId, final ResponseHandler<ScheduleData> handler) {
        String url = String.format("/schedule/host/%s", hostId);

        ApiHttpClient.get(url, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    handler.handle(gson.fromJson(response.toString(), ScheduleData.class));
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

    private static HostData extractHostData(JSONObject object) {
        return gson.fromJson(object.toString(), HostData.class);
    }

    public static void getHosts(final ResponseHandler<ArrayList<HostData>> handler) {
        ApiHttpClient.get("/user/hosts", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                ArrayList<HostData> result = new ArrayList<>(response.length());

                try {
                    for (int i = 0; i < response.length(); ++i) {
                        result.add(extractHostData((JSONObject) response.get(i)));
                    }
                    handler.handle(result);

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