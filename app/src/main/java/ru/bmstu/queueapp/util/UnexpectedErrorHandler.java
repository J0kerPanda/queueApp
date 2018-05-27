package ru.bmstu.queueapp.util;

import android.util.Log;

public final class UnexpectedErrorHandler {

    private UnexpectedErrorHandler() {}

    public static void handle(Throwable e) {
        Log.e("UNEXPECTED_ERROR", "", e);
    }

    public static void handle(Throwable e, String msg) {
        Log.e("UNEXPECTED_ERROR", msg, e);
    }
}
