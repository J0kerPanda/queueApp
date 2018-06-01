package ru.bmstu.queueapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import ru.bmstu.queueapp.http.data.UserData;

public class QueueApp extends Application {

    private static final String userIdKey = "userId";
    private static final String userEmailKey = "email";
    private static final String userFirstNameKey = "firstName";
    private static final String userSurnameKey = "surname";
    private static final String userPatronymicKey = "patronymic";
    private static final String userIsHostKey = "isHost";

    private static Application app;
    private static UserData user;
    private static SharedPreferences sharedPreferences;

    public void onCreate() {
        super.onCreate();
        QueueApp.app = this;
        QueueApp.sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        loadUser();
    }

    public static Context getAppContext() {
        return QueueApp.app.getApplicationContext();
    }

    public static synchronized void setUser(UserData user) {
        QueueApp.user = user;
        saveUser(user);
    }

    public static UserData getUser() {
        if (QueueApp.user == null) {
            loadUser();
        }
        return QueueApp.user;
    }

    private static void loadUser() {
        if (sharedPreferences != null) {
            if (sharedPreferences.getInt(userIdKey, -1 ) > 0) {
                setUser(new UserData(
                    sharedPreferences.getInt(userIdKey, -1),
                    sharedPreferences.getString(userEmailKey, ""),
                    sharedPreferences.getString(userFirstNameKey, ""),
                    sharedPreferences.getString(userSurnameKey, ""),
                    sharedPreferences.getString(userPatronymicKey, ""),
                    sharedPreferences.getBoolean(userIsHostKey, false)
                ));
            }
        }
    }

    private static void saveUser(UserData user) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(userIdKey, user.id);
        edit.putString(userEmailKey, user.email);
        edit.putString(userFirstNameKey, user.firstName);
        edit.putString(userSurnameKey, user.surname);
        edit.putString(userPatronymicKey, user.patronymic);
        edit.putBoolean(userIsHostKey, user.isHost);
        edit.apply();
    }

    public static void removeUser() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(userIdKey);
        edit.remove(userEmailKey);
        edit.remove(userFirstNameKey);
        edit.remove(userSurnameKey);
        edit.remove(userPatronymicKey);
        edit.remove(userIsHostKey);
        user = null;
        edit.apply();
    }
}
