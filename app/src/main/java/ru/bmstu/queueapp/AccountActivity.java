package ru.bmstu.queueapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ((TextView) findViewById(R.id.profileNameText)).setText(QueueApp.getUser().fullName());
    }

    public void logoutButtonHandler(View v) {
        QueueApp.removeUser();
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
