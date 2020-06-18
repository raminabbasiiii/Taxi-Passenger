package com.example.ramin.passenger.Activity;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ramin.passenger.MainActivity;
import com.example.ramin.passenger.Model.ReserveTripModel;
import com.example.ramin.passenger.Model.ResponseModel;
import com.example.ramin.passenger.Model.SearchTripsModel;
import com.example.ramin.passenger.Network.GetPassengerData;
import com.example.ramin.passenger.Network.RetrofitPassengerInstance;
import com.example.ramin.passenger.Preferences;
import com.example.ramin.passenger.R;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReserveTripActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar reserveTripToolbar;
    CircleImageView driverPicture;
    TextView tvDriverName,tvCarModel,tvOrigin,tvDestination,tvDateMovement,tvNumberDistance,tvMoney,reserveTripToolbarTitle,tvChairEmptyCount,tvShipmentCapacity,tvCooler,tvHeater,tvStopping,tvTripId,tvFinalMoney;
    Button btnReserveTrip,btnCashPay,btnWalletPay,btnOnlinePay;
    private static final String TAG ="TAG";
    List<ReserveTripModel> list ;
    Spinner chairSpinner;
    CheckBox barCheckBox;
    List<String> chairCount;
    String chairEmpty;
    int mul,money,finalMoney,emptyChairCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_trip);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setToolbar();
        findViews();
        getTripDetailsFromServer();
        getEmptyChairCount();
        //getChairReservedCountFromServer();
    }

    private void setToolbar() {
        reserveTripToolbar = findViewById(R.id.reserve_trip_toolbar);
        reserveTripToolbarTitle = reserveTripToolbar.findViewById(R.id.reserve_trip_toolbar_title);
        setSupportActionBar(reserveTripToolbar);
        reserveTripToolbarTitle.setText(R.string.reserve_trip_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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

    private void findViews() {
        driverPicture = findViewById(R.id.driver_picture);
        tvDriverName = findViewById(R.id.tv_driver_name);
        tvCarModel = findViewById(R.id.tv_car_model);
        tvOrigin = findViewById(R.id.tv_origin_reserve);
        tvDestination = findViewById(R.id.tv_destination_reserve);
        tvDateMovement = findViewById(R.id.tv_date_movement_reserve);
        tvNumberDistance = findViewById(R.id.tv_number_distance_reserve);
        tvMoney = findViewById(R.id.tv_money_reserve);
        tvChairEmptyCount = findViewById(R.id.tv_count_chair_empty_reserve);
        tvShipmentCapacity = findViewById(R.id.tv_shipment_capacity_reserve);
        tvCooler = findViewById(R.id.tv_have_cooler_reserve);
        tvHeater = findViewById(R.id.tv_have_heater_reserve);
        tvStopping = findViewById(R.id.tv_stopping_points_reserve);
        tvTripId = findViewById(R.id.tv_trip_id);

        btnReserveTrip = findViewById(R.id.btn_reserve_trip);
        btnReserveTrip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reserve_trip :
                runPayAlert();
                break;
        }
    }

    private void runPayAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReserveTripActivity.this,R.style.PayAlertDialog);
        builder.setView(R.layout.pay_money_alert_dialog)
                .setCancelable(true);

        final AlertDialog dialog = builder.create();
        dialog.show();

        chairSpinner = dialog.findViewById(R.id.chair_spinner);
        barCheckBox = dialog.findViewById(R.id.bar_checkbox);
        btnCashPay = dialog.findViewById(R.id.btn_cash_pay);
        btnOnlinePay = dialog.findViewById(R.id.btn_online_pay);
        btnWalletPay = dialog.findViewById(R.id.btn_wallet_pay);
        tvFinalMoney = dialog.findViewById(R.id.tv_money);

        chairCount = new ArrayList<>();
        chairCount.add("تعداد صندلی مورد نیاز را انتخاب کنید :");
        for (int i=1; i<emptyChairCount+1; i++) {
            chairCount.add(String.valueOf(i));
        }
        /*chairCount.add("1");
        chairCount.add("2");
        chairCount.add("3");
        chairCount.add("4");*/
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_item_selected,chairCount);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chairSpinner.setAdapter(adapter);
        chairSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        chairEmpty = parent.getItemAtPosition(position).toString();
                        mul = Integer.parseInt(chairEmpty);
                        money = list.get(0).getPrice();
                        finalMoney = mul * money;
                        DecimalFormat d = new DecimalFormat("###,###,###");
                        String rial = String.valueOf(d.format(finalMoney)) + " " + "ریال";
                        tvFinalMoney.setText(rial);
                        break;
                    case 2 :
                        chairEmpty = parent.getItemAtPosition(position).toString();
                        mul = Integer.parseInt(chairEmpty);
                        money = list.get(0).getPrice();
                        finalMoney = mul * money;
                        DecimalFormat dd = new DecimalFormat("###,###,###");
                        String riall = String.valueOf(dd.format(finalMoney)) + " " + "ریال";
                        tvFinalMoney.setText(riall);
                        break;
                    case 3:
                        chairEmpty = parent.getItemAtPosition(position).toString();
                        mul = Integer.parseInt(chairEmpty);
                        money = list.get(0).getPrice();
                        finalMoney = mul * money;
                        DecimalFormat ddd = new DecimalFormat("###,###,###");
                        String rialll = String.valueOf(ddd.format(finalMoney)) + " " + "ریال";
                        tvFinalMoney.setText(rialll);
                        break;
                    case 4:
                        chairEmpty = parent.getItemAtPosition(position).toString();
                        mul = Integer.parseInt(chairEmpty);
                        money = list.get(0).getPrice();
                        finalMoney = mul * money;
                        DecimalFormat dddd = new DecimalFormat("###,###,###");
                        String riallll = String.valueOf(dddd.format(finalMoney)) + " " + "ریال";
                        tvFinalMoney.setText(riallll);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnWalletPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inventory = MainActivity.inventory;
                if (finalMoney > inventory) {
                    Toast.makeText(getApplicationContext(),"اعبار حساب شما کافی نمی باشد. اعتبار حساب خود را افزایش دهید!!",Toast.LENGTH_LONG).show();
                } else {
                    Preferences preferences = new Preferences(ReserveTripActivity.this);
                    PersianCalendar now = new PersianCalendar();
                    int pId = preferences.getPassengerId();
                    int m = now.getPersianMonth() + 1;
                    String nowDate = now.getPersianYear() + "/" + m + "/" + now.getPersianDay();
                    String nowTime = now.getTime().getHours() + ":" + now.getTime().getMinutes();
                    String moneyPayment = String.valueOf(finalMoney);

                    GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
                    Call<ResponseModel> call = api.insertPayment(-(Integer.parseInt(moneyPayment)), pId, nowDate,nowTime);
                    call.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            switch (response.body().getResponse()) {
                                case "SUCCESS":
                                    Toast.makeText(getApplicationContext(), "هزینه سفر پرداخت شد.", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    break;
                                case "ERROR":
                                    Toast.makeText(getApplicationContext(), "پرداخت نشد. دوباره امتحان کنید!", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Log.i(TAG, "onReserveTripResponse: " + response.code() + " " + response.message());
                                    Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Log.i(TAG, "onFailure: " + t.getMessage());
                            Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    int tId = list.get(0).getTripId();
                    int sId = list.get(0).getSubId();
                    String paymentType = "کیف پول";
                    int chairCount = Integer.parseInt(chairEmpty);
                    String bar ;
                    if (barCheckBox.isChecked()) {
                        bar = "دارد";
                    } else {
                        bar = "ندارد";
                    }

                    GetPassengerData apii = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
                    Call<ResponseModel> calll = apii.insertReserveTrip(tId,pId,sId,paymentType,chairCount,bar,Integer.parseInt(moneyPayment));
                    calll.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            switch (response.body().getResponse()) {
                                case "SUCCESS":
                                    Toast.makeText(getApplicationContext(), "سفر شما رزرو شد.", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    break;
                                case "ERROR":
                                    Toast.makeText(getApplicationContext(), "سفر رزرو نشد. دوباره امتحان کنید!", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Log.i(TAG, "onReserveTripResponse: " + response.code() + " " + response.message());
                                    Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Log.i(TAG, "onFailure: " + t.getMessage());
                            Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btnOnlinePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"online payment",Toast.LENGTH_SHORT).show();
            }
        });

        btnCashPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences preferences = new Preferences(ReserveTripActivity.this);
                final int tId = list.get(0).getTripId();
                int pId = preferences.getPassengerId();
                final int sId = list.get(0).getSubId();

                String paymentType = "نقدی";
                final int chairCount = Integer.parseInt(chairEmpty);
                String bar ;
                if (barCheckBox.isChecked()) {
                    bar = "دارد";
                } else {
                    bar = "ندارد";
                }
                int cost = finalMoney;

                GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
                Call<ResponseModel> call = api.insertReserveTrip(tId,pId,sId,paymentType,chairCount,bar,cost);
                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        switch (response.body().getResponse()) {
                            case "SUCCESS":
                                Toast.makeText(getApplicationContext(), "سفر شما رزرو شد.", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                if (sId == 0) {
                                    GetPassengerData apii = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
                                    Call<ResponseModel> calll = apii.updateSubtripEmptyChairCount(tId,chairCount);
                                    calll.enqueue(new Callback<ResponseModel>() {
                                        @Override
                                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                                            Log.i(TAG, "onFailure: " + t.getMessage());
                                            Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                break;
                            case "ERROR":
                                Toast.makeText(getApplicationContext(), "سفر رزرو نشد. دوباره امتحان کنید!", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Log.i(TAG, "onReserveTripResponse: " + response.code() + " " + response.message());
                                Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Log.i(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getTripDetailsFromServer() {
        Bundle getId = getIntent().getExtras();
        if (getId != null) {
            int id = getId.getInt("tripId");
            int sId = getId.getInt("subId");
            GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
            Call<List<ReserveTripModel>> call = api.getTripDetails(id,sId);
            call.enqueue(new Callback<List<ReserveTripModel>>() {
                @Override
                public void onResponse(Call<List<ReserveTripModel>> call, Response<List<ReserveTripModel>> response) {
                    if (response.isSuccessful()) {
                        list = response.body();
                        String name = list.get(0).getDriverName() + " " + list.get(0).getDriverFamily();
                        String carModel = list.get(0).getDriverCarModel() + " " + list.get(0).getDriverCarColor();
                        String dateTime = list.get(0).getDateOfMovement() + "   " + list.get(0).getTimeOfMovement();
                        String distance = list.get(0).getDistance() + " " + "کیلومتر";
                        String image = list.get(0).getDriverImage();

                        int price = list.get(0).getPrice();
                        DecimalFormat d = new DecimalFormat("###,###,###");
                        String rial = String.valueOf(d.format(price)) + " " + "ریال";
                        int tripId = list.get(0).getTripId();
                        Glide.with(ReserveTripActivity.this).load(image).into(driverPicture);
                        tvDriverName.setText(name);
                        tvCarModel.setText(carModel);
                        tvOrigin.setText(list.get(0).getOrigin());
                        tvDestination.setText(list.get(0).getDestination());
                        tvNumberDistance.setText(distance);
                        tvDateMovement.setText(dateTime);
                        tvShipmentCapacity.setText(list.get(0).getShipmentCapacity());
                        tvCooler.setText(list.get(0).getCooler());
                        tvHeater.setText(list.get(0).getHeater());
                        tvStopping.setText(list.get(0).getStopping());
                        tvMoney.setText(String.valueOf(rial));
                        tvTripId.setText(String.valueOf(tripId));
                    } else {
                        Log.i(TAG, "onTripDetailsResponse: " + response.code() + " " + response.message());
                        Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<ReserveTripModel>> call, Throwable t) {
                    Log.i(TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getEmptyChairCount() {
        Bundle getId = getIntent().getExtras();
        if (getId != null) {
            int id = getId.getInt("tripId");
            int sId = getId.getInt("subId");

            GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
            Call<SearchTripsModel> call = api.getEmptyChairCount(id, sId);
            call.enqueue(new Callback<SearchTripsModel>() {
                @Override
                public void onResponse(Call<SearchTripsModel> call, Response<SearchTripsModel> response) {
                    if (response.isSuccessful()) {
                        SearchTripsModel model = response.body();
                        emptyChairCount = model.getEmptyChairCount();
                        tvChairEmptyCount.setText(String.valueOf(emptyChairCount));
                    }else {
                        Log.i(TAG, "onTripsResponse: " + response.code() + " " + response.message());
                        Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SearchTripsModel> call, Throwable t) {
                    Log.i(TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
