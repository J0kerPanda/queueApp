package ru.bmstu.queueapp.http.error;

import android.util.Log;

import org.jetbrains.annotations.Nullable;

import ru.bmstu.queueapp.QueueApp;

public final class DefaultErrorHandler {

    public static void handleHttp(int code, Throwable e, @Nullable Object response) {
        Log.d("API_ERROR", String.format("%d: %s", code, e.getMessage(), response == null ? "" : response.toString()));
        switch (code) {
            case 403:
                QueueApp.logout();
                break;
        }
    }

    public static void handle(Throwable e) {
        Log.d("UNEXPECTED_ERROR", e.getMessage());
    }

    private DefaultErrorHandler() {}
}
