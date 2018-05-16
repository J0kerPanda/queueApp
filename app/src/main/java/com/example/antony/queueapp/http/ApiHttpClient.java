package com.example.antony.queueapp.http;

import android.content.Context;
import android.util.Log;

import com.example.antony.queueapp.http.data.Appointment;
import com.example.antony.queueapp.http.data.HostData;
import com.example.antony.queueapp.http.data.SchedulesData;
import com.example.antony.queueapp.http.request.AppointmentsRequest;
import com.example.antony.queueapp.http.request.CreateAppointmentRequest;
import com.example.antony.queueapp.util.UnexpectedErrorHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ApiHttpClient {
    private static final String BASE_URL = "http://192.168.1.37:9000/api";

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static final Gson gson = new JsonExtractor().gson;

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static <T> void post(Context context, String url, T entity, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException {
        String entityStr = gson.toJson(entity);
        client.post(context, getAbsoluteUrl(url), new StringEntity(entityStr), ContentType.APPLICATION_JSON.getMimeType(), responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        Log.d("MY_CUSTOM_LOG", BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }

    public static void getScheduleData(String hostId, final ResponseHandler<SchedulesData> handler) {
        String url = String.format("/schedule/host/%s", hostId);

        ApiHttpClient.get(url, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    handler.handle(gson.fromJson(response.toString(), SchedulesData.class));
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

    public static void getHosts(final ResponseHandler<ArrayList<HostData>> handler) {
        ApiHttpClient.get("/user/hosts", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    ArrayList<HostData> result = gson.fromJson(response.toString(), new TypeToken<ArrayList<HostData>>(){}.getType());
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

    public static void getAppointments(final Context context,
                                       final AppointmentsRequest req,
                                       final ResponseHandler<ArrayList<Appointment>> handler) {

        try {
            ApiHttpClient.post(context, "/appointment/list", req, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {
                        ArrayList<Appointment> result = gson.fromJson(
                            response.toString(),
                            new TypeToken<ArrayList<Appointment>>(){}.getType()
                        );
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
                public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable e) {
                    UnexpectedErrorHandler.handle(e);
                }
            });
        } catch (UnsupportedEncodingException e) {
            UnexpectedErrorHandler.handle(e);
        }
    }

    public static void createAppointment(final Context context,
                                         final CreateAppointmentRequest req,
                                         final ResponseHandler<Boolean> handler) {

        try {
            ApiHttpClient.post(context, "/appointment/create", req, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    handler.handle(true);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    handler.handle(false);
                }
            });
        } catch (UnsupportedEncodingException e) {
            UnexpectedErrorHandler.handle(e);
        }
    }
}