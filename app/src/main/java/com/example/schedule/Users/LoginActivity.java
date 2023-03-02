package com.example.schedule.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schedule.R;

public class LoginActivity extends AppCompatActivity {

    TextView forgotPwd, signUpLink;
    Button loginBtn;
    EditText email, password;

    boolean valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgotPwd = findViewById(R.id.forgot_pwd);
        signUpLink = findViewById(R.id.signup_link);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

        loginBtn = findViewById(R.id.login_btn);
        valid = true;

        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if (userEmail.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Email Is Empty", Toast.LENGTH_SHORT).show();
                    email.setBackground(getDrawable(R.drawable.ivalid));
                    valid = false;
                }else{
                    email.setBackground(getDrawable(R.drawable.edit_text_background));
                }
                if(userPassword.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Password Required", Toast.LENGTH_SHORT).show();
                    password.setBackground(getDrawable(R.drawable.ivalid));
                    valid = false;
                }else{
                    password.setBackground(getDrawable(R.drawable.edit_text_background));
                }
                if (userPassword.length() < 6){
                    Toast.makeText(LoginActivity.this, "Password Should be more than six", Toast.LENGTH_SHORT).show();
                    password.setBackground(getDrawable(R.drawable.ivalid));
                    valid = false;
                }else{
                    password.setBackground(getDrawable(R.drawable.edit_text_background));
                }
            }
        });
    }
}