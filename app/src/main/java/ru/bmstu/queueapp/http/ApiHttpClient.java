package ru.bmstu.queueapp.http;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import ru.bmstu.queueapp.QueueApp;
import ru.bmstu.queueapp.http.data.Appointment;
import ru.bmstu.queueapp.http.data.SchedulesData;
import ru.bmstu.queueapp.http.data.UserData;
import ru.bmstu.queueapp.http.request.CreateAppointmentRequest;
import ru.bmstu.queueapp.http.request.LoginRequest;
import ru.bmstu.queueapp.util.UnexpectedErrorHandler;

public class ApiHttpClient {
    private static final String BASE_URL = "http://192.168.1.5:9000/api";
    private static final Gson gson = new JsonExtractor().gson;

    private PersistentCookieStore cookieStore;
    private AsyncHttpClient client;
    private static ApiHttpClient instance;

    private ApiHttpClient() {
        client = new AsyncHttpClient();
        cookieStore = new PersistentCookieStore(QueueApp.getAppContext());
        client.setCookieStore(cookieStore);
    }

    public static ApiHttpClient instance() {
        if (instance == null) {
            instance = new ApiHttpClient();
        }
        return instance;
    }
    
    public void clearCookies() {
        cookieStore.clear();
    }

    private void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private <T> void post(Context context, String url, T entity, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException {
        String entityStr = gson.toJson(entity);
        client.post(context, getAbsoluteUrl(url), new StringEntity(entityStr), ContentType.APPLICATION_JSON.getMimeType(), responseHandler);
    }

    private String getAbsoluteUrl(String relativeUrl) {
        Log.d("MY_CUSTOM_LOG", BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }

    public void getScheduleData(String hostId, final ResponseHandler<SchedulesData> handler) {
        String url = String.format("/schedule/host/%s", hostId);

        get(url, new RequestParams(), new JsonHttpResponseHandler() {

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

    public void getHosts(final ResponseHandler<ArrayList<UserData>> handler) {
        get("/user/hosts", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    ArrayList<UserData> result = gson.fromJson(response.toString(), new TypeToken<ArrayList<UserData>>(){}.getType());
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

    public void getAppointments(ArrayList<Integer> scheduleIds,
                                final ResponseHandler<ArrayList<Appointment>> handler) {

        Log.i("MY_CUSTOM_LOG", scheduleIds.toString());

        try {
            post(QueueApp.getAppContext(),"/appointment/list", scheduleIds, new JsonHttpResponseHandler() {
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

    public void createAppointment(final CreateAppointmentRequest req, final ResponseHandler<Boolean> handler) {

        Log.i("MY_CUSTOM_LOG", req.toString());

        try {
            post(QueueApp.getAppContext(), "/appointment/create", req, new AsyncHttpResponseHandler() {
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

    public void login(final LoginRequest loginRequest, final ResponseHandler<UserData> handler) {

        Log.i("MY_CUSTOM_LOG", loginRequest.toString());

        try {
            post(QueueApp.getAppContext(), "/user/login", loginRequest, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        handler.handle(gson.fromJson(response.toString(), UserData.class));
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
}