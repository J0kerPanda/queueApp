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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import ru.bmstu.queueapp.QueueApp;
import ru.bmstu.queueapp.http.data.AccountAppointment;
import ru.bmstu.queueapp.http.data.Appointment;
import ru.bmstu.queueapp.http.data.RepeatedSchedule;
import ru.bmstu.queueapp.http.data.RepeatedSchedulesData;
import ru.bmstu.queueapp.http.data.Schedule;
import ru.bmstu.queueapp.http.data.SchedulesData;
import ru.bmstu.queueapp.http.data.UserData;
import ru.bmstu.queueapp.http.error.DefaultErrorHandler;
import ru.bmstu.queueapp.http.request.CreateAppointmentRequest;
import ru.bmstu.queueapp.http.request.LoginRequest;

public class ApiHttpClient {
//    private static final String BASE_URL = "https://queue-service.herokuapp.com/api";
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

    private <T> void post(Context context, String url, T entity, AsyncHttpResponseHandler responseHandler) {
        String entityStr = gson.toJson(entity);
        client.post(context, getAbsoluteUrl(url), new StringEntity(entityStr, StandardCharsets.UTF_8), ContentType.APPLICATION_JSON.getMimeType(), responseHandler);
    }

    private void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(context, getAbsoluteUrl(url), null, params, ContentType.TEXT_PLAIN.getMimeType(), responseHandler);
    }

    private String getAbsoluteUrl(String relativeUrl) {
        Log.d("MY_CUSTOM_LOG", BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }

    public void getScheduleData(int hostId, final ResponseHandler<SchedulesData> handler) {
        String url = String.format("/schedule/host/%d", hostId);

        get(url, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    Log.d("my_custom_log", response.toString());
                    handler.handle(gson.fromJson(response.toString(), SchedulesData.class));
                } catch (Exception e) {
                    DefaultErrorHandler.handle(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, responseString);
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
                    DefaultErrorHandler.handle(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, responseString);
            }
        });
    }

    public void getAppointments(int scheduleId, final ResponseHandler<ArrayList<Appointment>> handler) {

        Log.i("MY_CUSTOM_LOG", String.valueOf(scheduleId));

        get("/appointment/list", new RequestParams("scheduleId", String.valueOf(scheduleId)), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    ArrayList<Appointment> result = gson.fromJson(
                        response.toString(),
                        new TypeToken<ArrayList<Appointment>>(){}.getType()
                    );
                    handler.handle(result);
                } catch (Exception e) {
                    DefaultErrorHandler.handle(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, responseString);
            }
        });
    }

    public void getUserAppointments(int userId, final ResponseHandler<ArrayList<AccountAppointment>> handler) {

        Log.i("MY_CUSTOM_LOG", String.valueOf(userId));

        get("/appointment/visitor", new RequestParams("id", String.valueOf(userId)), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    ArrayList<AccountAppointment> result = gson.fromJson(
                        response.toString(),
                        new TypeToken<ArrayList<AccountAppointment>>(){}.getType()
                    );
                    handler.handle(result);
                } catch (Exception e) {
                    DefaultErrorHandler.handle(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, responseString);
            }
        });
    }

    public void createAppointment(final CreateAppointmentRequest req, final ResponseHandler<Boolean> handler) {

        Log.i("MY_CUSTOM_LOG", req.toString());

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
    }

    public void cancelAppointment(long appointmentId, final ResponseHandler<Boolean> handler) {

        String url = String.format("/appointment/cancel/%d", appointmentId);

        post(QueueApp.getAppContext(), url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                handler.handle(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.handle(false);
            }
        });
    }

    public void login(final LoginRequest loginRequest, final ResponseHandler<UserData> handler) {

        Log.i("MY_CUSTOM_LOG", loginRequest.toString());

        post(QueueApp.getAppContext(), "/user/login", loginRequest, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    handler.handle(gson.fromJson(response.toString(), UserData.class));
                } catch (Exception e) {
                    DefaultErrorHandler.handle(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, responseString);
            }
        });
    }

    public void createSchedule(final Schedule schedule, final ResponseHandler<SchedulesData> handler) {

        Log.d("MY_CUSTOM_LOG", schedule.toString());

        post(QueueApp.getAppContext(), "/schedule/create", schedule, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    handler.handle(gson.fromJson(response.toString(), SchedulesData.class));
                } catch (Exception e) {
                    DefaultErrorHandler.handle(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, responseString);
            }
        });
    }

    public void updateSchedule(final Schedule schedule, final ResponseHandler<SchedulesData> handler) {

        Log.d("MY_CUSTOM_LOG", schedule.toString());

        post(QueueApp.getAppContext(), "/schedule/update", schedule, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    handler.handle(gson.fromJson(response.toString(), SchedulesData.class));
                } catch (Exception e) {
                    DefaultErrorHandler.handle(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, responseString);
            }
        });
    }

    public void deleteSchedule(Integer scheduleId, final ResponseHandler<SchedulesData> handler) {
        String url = String.format("/schedule/delete/%d", scheduleId);

        post(QueueApp.getAppContext(), url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    handler.handle(gson.fromJson(response.toString(), SchedulesData.class));
                } catch (Exception e) {
                    DefaultErrorHandler.handle(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, responseString);
            }
        });
    }

    public void getRepeatedScheduleData(int hostId, final ResponseHandler<RepeatedSchedulesData> handler) {
        String url = String.format("/schedule/repeated/host/%d", hostId);

        get(url, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    Log.d("my_custom_log", response.toString());
                    handler.handle(gson.fromJson(response.toString(), RepeatedSchedulesData.class));
                } catch (Exception e) {
                    DefaultErrorHandler.handle(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, responseString);
            }
        });
    }

    public void createRepeatedSchedule(final RepeatedSchedule schedule, final ResponseHandler<RepeatedSchedulesData> handler) {

        Log.d("MY_CUSTOM_LOG", schedule.toString());

        post(QueueApp.getAppContext(), "/schedule/repeated/create", schedule, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    handler.handle(gson.fromJson(response.toString(), RepeatedSchedulesData.class));
                } catch (Exception e) {
                    DefaultErrorHandler.handle(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                DefaultErrorHandler.handleHttp(statusCode, throwable, responseString);
            }
        });
    }
}