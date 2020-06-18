package com.example.ramin.passenger.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ramin.passenger.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginOrSignUpActivity extends AppCompatActivity {

    Button btnLogin,btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_sign_up);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViews();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void findViews() {
        btnLogin = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_sign_up);
    }

    public void btnLoginOnClick(View v) {
        Intent loginActivity = new Intent(LoginOrSignUpActivity.this,LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    public void btnSignUpOnClick(View v) {
        Intent signUpActivity = new Intent(LoginOrSignUpActivity.this,SignUpActivity.class);
        startActivity(signUpActivity);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
