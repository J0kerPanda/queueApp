package ru.bmstu.queueapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import ru.bmstu.queueapp.http.ApiHttpClient;
import ru.bmstu.queueapp.http.ResponseHandler;
import ru.bmstu.queueapp.http.data.UserData;

public class AccountAppointmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_appointments);

//        ApiHttpClient.instance().getHosts(new ResponseHandler<ArrayList<UserData>>() {
//            @Override
//            public void handle(ArrayList<UserData> result) {
//                ApiHttpClient.instance().
//            }
//        });
    }
}
