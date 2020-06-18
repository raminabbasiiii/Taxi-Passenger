package com.example.ramin.passenger.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ramin.passenger.MainActivity;
import com.example.ramin.passenger.Model.PassengerModel;
import com.example.ramin.passenger.Network.GetPassengerData;
import com.example.ramin.passenger.Network.RetrofitPassengerInstance;
import com.example.ramin.passenger.Preferences;
import com.example.ramin.passenger.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    EditText etPassword,etMail;
    Button btnLogin;
    String mail,password;
    private static final String TAG ="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViews();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void findViews() {
        etMail = findViewById(R.id.et_mail);
        etPassword = findViewById(R.id.et_pass);
        btnLogin = findViewById(R.id.btn_login);
    }

    public void btnLoginOnClick(View v) {
        mail = etMail.getText().toString();
        password = etPassword.getText().toString();
        GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
        Call<PassengerModel> call = api.passengerLogin(mail,password);
        call.enqueue(new Callback<PassengerModel>() {
            @Override
            public void onResponse(Call<PassengerModel> call, Response<PassengerModel> response) {
                if (response.body().getResponse().equals("SUCCESS")) {
                    PassengerModel model = response.body();
                    Preferences preferences = new Preferences(LoginActivity.this);
                    preferences.setPassengerSharedPreferences(model.getPassengerId(),model.getPassengerName(),model.getPassengerFamily(),model.getPassengerSexuality(),model.getPassengerMail(),model.getPassengerMobile(),model.getPassengerPassword());
                    Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainActivity);
                    finish();
                } else if (response.body().getResponse().equals("FAILED")) {
                    Toast.makeText(getApplicationContext(),"ایمیل یا گذرواژه را اشتباه وارد کرده اید!!!",Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG, "onLoginResponse: " + response.code() + " " + response.message());
                    Toast.makeText(LoginActivity.this, "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PassengerModel> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent loginOrSignUpActivity = new Intent(LoginActivity.this, LoginOrSignUpActivity.class);
        startActivity(loginOrSignUpActivity);
        finish();
    }
}
