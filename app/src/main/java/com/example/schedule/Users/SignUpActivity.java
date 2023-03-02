package com.example.schedule.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.schedule.MainActivity;
import com.example.schedule.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText signUpEmail, signUpPassword, signUpUsername;
    private TextView signUpLater, AlreadyHaveAnAccount;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpPassword = findViewById(R.id.signup_password);
        signUpButton = findViewById(R.id.signup_btn);
        signUpEmail = findViewById(R.id.signup_email);
        signUpLater = findViewById(R.id.signup_later);
        signUpUsername = findViewById(R.id.signup_username);
        AlreadyHaveAnAccount = findViewById(R.id.alreadyHaveAnAccount);

        AlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        signUpLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}