package ru.bmstu.queueapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.bmstu.queueapp.http.ApiHttpClient;
import ru.bmstu.queueapp.http.ResponseHandler;
import ru.bmstu.queueapp.http.request.LoginRequest;
import ru.bmstu.queueapp.http.data.UserData;

public class LoginActivity extends AppCompatActivity {

    private TextView loginInput;
    private TextView passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginInput = findViewById(R.id.loginInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        loginInput.addTextChangedListener(nonEmptyTextWatcher);
        passwordInput.addTextChangedListener(nonEmptyTextWatcher);

        if (QueueApp.getUser() != null) {
            moveToMainActivity();
        }
    }

    public void loginButtonHandler(View v) {

        LoginRequest loginRequest = new LoginRequest(loginInput.getText().toString(), passwordInput.getText().toString());

        Log.d("MY_CUSTOM_LOG", loginRequest.email);

        ApiHttpClient.instance().login(loginRequest, new ResponseHandler<UserData>() {
            @Override
            public void handle(UserData result) {
                Log.d("MY_CUSTOM_LOG", result.fullData());
                QueueApp.setUser(result);
                moveToMainActivity();
            }
        });
    }

    public final TextWatcher nonEmptyTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            loginButton.setEnabled(s.length() > 0);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void moveToMainActivity() {
        Intent intent = new Intent(getBaseContext(), SelectHostActivity.class);
        startActivity(intent);
    }
}
