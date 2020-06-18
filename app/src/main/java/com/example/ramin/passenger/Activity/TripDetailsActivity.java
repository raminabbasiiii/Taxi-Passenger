package com.example.ramin.passenger.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ramin.passenger.Model.DoTripDetailsModel;
import com.example.ramin.passenger.Network.GetPassengerData;
import com.example.ramin.passenger.Network.RetrofitPassengerInstance;
import com.example.ramin.passenger.R;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TripDetailsActivity extends AppCompatActivity {

    Toolbar tripDetailToolbar;
    CircleImageView pictureDriver;
    ImageButton imgShowPlace;
    TextView tvDriverName,tvCarModel,tvOrigin,tvDestination,tvDateTime,tvDistance,tvMoney,tvPayType,tvMoneyReduction,tvMoneyAllTrip,tvTripId,tripDetailToolbarTitle;
    List<DoTripDetailsModel> list;
    private static final String TAG ="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setToolbar();
        findViews();
        getDoTripDetailsFromServer();
    }

    private void setToolbar() {
        tripDetailToolbar = findViewById(R.id.trip_detail_toolbar);
        tripDetailToolbarTitle = tripDetailToolbar.findViewById(R.id.trip_detail_toolbar_title);
        setSupportActionBar(tripDetailToolbar);
        tripDetailToolbarTitle.setText(R.string.trip_detail_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void findViews() {
        pictureDriver = findViewById(R.id.driver_picture);
        imgShowPlace = findViewById(R.id.img_show_place);
        tvDriverName = findViewById(R.id.tv_driver_name);
        tvCarModel = findViewById(R.id.tv_car_model);
        tvOrigin = findViewById(R.id.tv_origin_detail);
        tvDestination = findViewById(R.id.tv_destination_detail);
        tvDateTime = findViewById(R.id.tv_date_movement_detail);
        tvDistance = findViewById(R.id.tv_number_distance_detail);
        tvMoney = findViewById(R.id.tv_money_detail);
        tvPayType = findViewById(R.id.tv_pay_type_detail);
        tvMoneyReduction = findViewById(R.id.tv_money_reduction_detail);
        tvMoneyAllTrip = findViewById(R.id.tv_money_all_trip_detail);
        tvTripId = findViewById(R.id.tv_trip_id_detail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
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

    private void getDoTripDetailsFromServer() {
        Bundle getId = getIntent().getExtras();
        if (getId != null) {
            int id = getId.getInt("tripId");
            int sId = getId.getInt("subId");
            GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
            Call<List<DoTripDetailsModel>> call = api.getDoTripDetails(id,sId);
            call.enqueue(new Callback<List<DoTripDetailsModel>>() {
                @Override
                public void onResponse(Call<List<DoTripDetailsModel>> call, Response<List<DoTripDetailsModel>> response) {
                    if (response.isSuccessful()) {
                        list = response.body();
                        String name = list.get(0).getDriverName() + " " + list.get(0).getDriverFamily();
                        String carModel = list.get(0).getDriverCarModel() + " " + list.get(0).getDriverCarColor();
                        String dateTime = list.get(0).getDateOfMovement() + "   " + list.get(0).getTimeOfMovement();
                        String distance = list.get(0).getDistance() + " " + "کیلومتر";
                        String image = list.get(0).getDriverImage();
                        int takhfif = 0;
                        String t = String.valueOf(takhfif) + " " + "ریال";
                        int price = list.get(0).getCostOfPayment();
                        DecimalFormat d = new DecimalFormat("###,###,###");
                        String rial = String.valueOf(d.format(price)) + " " + "ریال";
                        int tripId = list.get(0).getTripId();
                        Glide.with(TripDetailsActivity.this).load(image).into(pictureDriver);
                        tvDriverName.setText(name);
                        tvCarModel.setText(carModel);
                        tvOrigin.setText(list.get(0).getOrigin());
                        tvDestination.setText(list.get(0).getDestination());
                        tvDateTime.setText(dateTime);
                        tvDistance.setText(distance);
                        tvMoney.setText(rial);
                        tvPayType.setText(list.get(0).getPaymentType());
                        tvTripId.setText(String.valueOf(tripId));
                        tvMoneyReduction.setText(t);
                        int finalMoneyPay = price + takhfif;
                        String riall = String.valueOf(d.format(finalMoneyPay)) + " " + "ریال";
                        tvMoneyAllTrip.setText(riall);
                    }else {
                        Log.i(TAG, "onTripDetailsResponse: " + response.code() + " " + response.message());
                        Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<DoTripDetailsModel>> call, Throwable t) {
                    Log.i(TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
