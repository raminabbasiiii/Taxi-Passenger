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

import com.example.ramin.passenger.Model.PassengerModel;
import com.example.ramin.passenger.Network.GetPassengerData;
import com.example.ramin.passenger.Network.RetrofitPassengerInstance;
import com.example.ramin.passenger.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends AppCompatActivity {

    EditText etName,etFamily,etSexuality,etMail,etMobile,etPassword;
    Button btnSignUp;
    String name,family,sexuality,mail,mobile,password;
    private static final String TAG ="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViews();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void findViews() {
        etFamily = findViewById(R.id.et_family);
        etMail = findViewById(R.id.et_mail);
        etMobile = findViewById(R.id.et_mobile);
        etName = findViewById(R.id.et_name);
        etPassword = findViewById(R.id.et_pass);
        etSexuality = findViewById(R.id.et_sexuality);
        btnSignUp = findViewById(R.id.btn_sign_up);
    }

    public void btnSignUpOnClick(View v) {
        name = etName.getText().toString();
        family = etFamily.getText().toString();
        sexuality = etSexuality.getText().toString();
        mail = etMail.getText().toString();
        mobile = etMobile.getText().toString();
        password = etPassword.getText().toString();

        GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
        Call<PassengerModel> call = api.signUpPassenger(name,family,sexuality,mail,mobile,password);
        call.enqueue(new Callback<PassengerModel>() {
            @Override
            public void onResponse(Call<PassengerModel> call, Response<PassengerModel> response) {
                switch (response.body().getResponse()) {
                    case "REGISTERED":
                        Toast.makeText(getApplicationContext(), "این گذرواژه قبلا ثبت شده است.گذرواژه جدید را وارد کنید!", Toast.LENGTH_LONG).show();
                        break;
                    case "SUCCESS":
                        Toast.makeText(getApplicationContext(), "عضویت با موفقیت انجام شد!!", Toast.LENGTH_LONG).show();
                        break;
                    case "ERROR":
                        Toast.makeText(getApplicationContext(), "عضویت انجام نشد. لطفا دوباره امتحان کنید!!", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Log.i(TAG, "onSignUpResponse: " + response.code() + " " + response.message());
                        Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<PassengerModel> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent loginOrSignUpActivity = new Intent(SignUpActivity.this, LoginOrSignUpActivity.class);
        startActivity(loginOrSignUpActivity);
        finish();
    }
}
