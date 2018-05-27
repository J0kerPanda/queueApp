package com.example.antony.queueapp;

import android.app.Application;
import android.content.Context;

import com.example.antony.queueapp.http.data.UserData;

public class QueueApp extends Application {

    private static Application app;
    private static UserData user;

    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static Context getAppContext() {
        return QueueApp.app.getApplicationContext();
    }

    public static void setUser(UserData user) {
        QueueApp.user = user;
    }

    public static UserData getUser() {
        return QueueApp.user;
    }
}
