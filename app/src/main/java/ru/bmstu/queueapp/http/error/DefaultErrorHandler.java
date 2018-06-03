package ru.bmstu.queueapp.http.error;

import android.util.Log;

public final class DefaultErrorHandler {

    private DefaultErrorHandler() {}

    public static void handle(Throwable e) {
        Log.e("UNEXPECTED_ERROR", "", e);
    }

    public static void handle(Throwable e, String msg) {
        Log.e("UNEXPECTED_ERROR", msg, e);
    }
}
