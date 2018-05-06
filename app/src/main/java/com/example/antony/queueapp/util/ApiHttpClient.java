package com.example.antony.queueapp.util;

import android.util.Log;

import com.loopj.android.http.*;

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
}