package ru.bmstu.queueapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ((TextView) findViewById(R.id.profileNameText)).setText(QueueApp.getUser().fullName());
        if (!QueueApp.getUser().isHost) {
            findViewById(R.id.accountSchedulesButton).setVisibility(View.GONE);
            findViewById(R.id.accountRepeatedSchedulesButton).setVisibility(View.GONE);
        }
    }

    public void accountAppointmentsButtonHandler(View v) {
        Intent intent = new Intent(getBaseContext(), AccountAppointmentsActivity.class);
        startActivity(intent);
    }

    public void accountSchedulesButtonHandler(View v) {
        Intent intent = new Intent(getBaseContext(), AccountSchedulesActivity.class);
        startActivity(intent);
    }

    public void accountRepeatedSchedulesButtonHandler(View v) {
        Intent intent = new Intent(getBaseContext(), AccountRepeatedSchedulesActivity.class);
        startActivity(intent);
    }

    public void logoutButtonHandler(View v) {
        QueueApp.removeUser();
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
