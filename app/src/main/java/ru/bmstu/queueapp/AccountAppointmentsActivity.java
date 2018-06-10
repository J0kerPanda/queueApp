package ru.bmstu.queueapp;

import android.accounts.Account;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import ru.bmstu.queueapp.adapters.AccountAppointmentItemAdapter;
import ru.bmstu.queueapp.http.ApiHttpClient;
import ru.bmstu.queueapp.http.ResponseHandler;
import ru.bmstu.queueapp.http.data.HostAppointment;
import ru.bmstu.queueapp.http.data.UserData;

public class AccountAppointmentsActivity extends AppCompatActivity {

    private ListView accountAppointmentsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_appointments);

        accountAppointmentsListView = findViewById(R.id.accountAppointmentsView);

        ApiHttpClient.instance().getUserAppointments(QueueApp.getUser().id, new ResponseHandler<ArrayList<HostAppointment>>() {
            @Override
            public void handle(ArrayList<HostAppointment> result) {
                AccountAppointmentItemAdapter adapter = new AccountAppointmentItemAdapter(getBaseContext(), result);
                accountAppointmentsListView.setAdapter(adapter);
            }
        });
    }
}
