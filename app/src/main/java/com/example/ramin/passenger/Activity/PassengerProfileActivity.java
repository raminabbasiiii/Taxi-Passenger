package com.example.ramin.passenger.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramin.passenger.MainActivity;
import com.example.ramin.passenger.Model.PassengerModel;
import com.example.ramin.passenger.Model.ResponseModel;
import com.example.ramin.passenger.Network.GetPassengerData;
import com.example.ramin.passenger.Network.RetrofitPassengerInstance;
import com.example.ramin.passenger.Preferences;
import com.example.ramin.passenger.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PassengerProfileActivity extends AppCompatActivity {

    Toolbar passengerProfileToolbar;
    EditText etName,etFamily,etMobile,etMail,etSexuality,etPassword;
    TextView passengerProfileToolbarTitle;
    private static final String TAG ="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_profile);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setToolbar();
        findViews();
    }

    private void setToolbar() {
        passengerProfileToolbar = findViewById(R.id.passenger_profile_toolbar);
        passengerProfileToolbarTitle = passengerProfileToolbar.findViewById(R.id.passenger_profile_toolbar_title);
        setSupportActionBar(passengerProfileToolbar);
        passengerProfileToolbarTitle.setText(R.string.passenger_profile_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void findViews() {
        etName = findViewById(R.id.et_name);
        etFamily = findViewById(R.id.et_family);
        etMobile = findViewById(R.id.et_mobile);
        etMail = findViewById(R.id.et_mail);
        etSexuality = findViewById(R.id.et_sexuality);
        etPassword = findViewById(R.id.et_pass);

        Preferences p = new Preferences(this);
        etName.setText(p.getPassengerName());
        etFamily.setText(p.getPassengerFamily());
        etMobile.setText(p.getPassengerMobile());
        etMail.setText(p.getPassengerMail());
        etSexuality.setText(p.getPassengerSexuality());
        etPassword.setText(p.getPassengerPassword());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        }
        else if (id == R.id.check_passenger_profile){

            final Preferences p = new Preferences(PassengerProfileActivity.this);
            final String name = etName.getText().toString();
            final String family= etFamily.getText().toString();
            final String sexuality = etSexuality.getText().toString();
            final String mail = etMail.getText().toString();
            final String password = etPassword.getText().toString();
            final String mobile = etMobile.getText().toString();
            final int pId = p.getPassengerId();

            GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
            Call<ResponseModel> call = api.updateProfile(pId,name,family,sexuality,mail,mobile,password);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.body().getResponse().equals("SUCCESS")) {
                        p.setPassengerSharedPreferences(pId,name,family,sexuality,mail,mobile,password);
                        Toast.makeText(getApplicationContext(),"اطلاعات شما با موفقیت ویرایش گردید!",Toast.LENGTH_LONG).show();

                    } else if (response.body().getResponse().equals("FAILED")) {
                        Toast.makeText(getApplicationContext(),"اطلاعات شما ویرایش نشد. دوباره امتحان کنید!!",Toast.LENGTH_LONG).show();
                    } else {
                        Log.i(TAG, "onUpdateProfileResponse: " + response.code() + " " + response.message());
                        Toast.makeText(PassengerProfileActivity.this, "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.i(TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(PassengerProfileActivity.this, "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
