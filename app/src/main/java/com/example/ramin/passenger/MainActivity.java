package com.example.ramin.passenger;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramin.passenger.Activity.AboutMeActivity;
import com.example.ramin.passenger.Activity.LoginOrSignUpActivity;
import com.example.ramin.passenger.Activity.PassengerProfileActivity;
import com.example.ramin.passenger.Activity.TransactionActivity;
import com.example.ramin.passenger.Activity.TripsActivity;
import com.example.ramin.passenger.Adapter.RecyclerSearchTripsAdapter;
import com.example.ramin.passenger.Model.PassengerFinancialAccountModel;
import com.example.ramin.passenger.Model.ResponseModel;
import com.example.ramin.passenger.Model.SearchTripsModel;
import com.example.ramin.passenger.Network.GetPassengerData;
import com.example.ramin.passenger.Network.RetrofitPassengerInstance;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mainDrawerLayout;
    NavigationView mainNavigationView;
    Toolbar mainToolbar;
    TabLayout mainTabLayout;
    ViewPager mainViewPager;
    MainViewPagerAdapter viewPagerAdapter;
    TextView tvCredit;
    EditText etIncreaseCredit;
    Button btnIncreaseCredit,btnCloseWallet;
    public static int inventory = 0;
    private static final String TAG ="TAG";
    String getOrigin,getDestination,getDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setToolbar();
        setNavigationDrawer();
        setTabLayout();
        getInventoryFromServer();

        Preferences p = new Preferences(this);

        if (p.getPassengerPassword() == null && p.getPassengerMail() == null) {
            Intent loginOrSignUpActivity = new Intent(MainActivity.this, LoginOrSignUpActivity.class);
            startActivity(loginOrSignUpActivity);
            finish();
        }

        /*Bundle getData = getIntent().getExtras();
        if (getData != null) {
            getOrigin = getData.getString("origin");
            getDestination = getData.getString("destination");
            getDistance = getData.getString("distance");
        }*/

    }

    /*public String getGetDestination() {
        return getDestination;
    }

    public String getGetOrigin() {
        return getOrigin;
    }

    public String getGetDistance() {
        return getDistance;
    }*/

    private void setToolbar() {
        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
    }

    private void setNavigationDrawer() {
        mainDrawerLayout = findViewById(R.id.main_drawer_layout);
        mainNavigationView = findViewById(R.id.main_navigation_view);
        mainNavigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle mainToggle = new ActionBarDrawerToggle(this,mainDrawerLayout,mainToolbar,R.string.open,R.string.close);
        mainDrawerLayout.addDrawerListener(mainToggle);
        mainToggle.syncState();
    }

    private void setTabLayout() {
        mainTabLayout = findViewById(R.id.main_tab_layout);
        mainViewPager = findViewById(R.id.main_view_pager);
        viewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new SearchTripsFragment(),"جستجوی سفر");
        viewPagerAdapter.addFrag(new RegisteredTripsFragment(),"سفرهای رزرو شده");
        mainViewPager.setAdapter(viewPagerAdapter);
        mainTabLayout.setupWithViewPager(mainViewPager);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.profile :
                Intent passengerProfileActivity = new Intent(MainActivity.this,PassengerProfileActivity.class);
                startActivity(passengerProfileActivity);
                break;

            case R.id.wallet :
                runWalletAlert();
                break;

            case R.id.trips :
                Intent tripsActivity = new Intent(MainActivity.this,TripsActivity.class);
                startActivity(tripsActivity);
                break;

            case R.id.transaction :
                Intent transactionActivity = new Intent(MainActivity.this,TransactionActivity.class);
                startActivity(transactionActivity);
                break;

            case R.id.settings :
                Toast.makeText(getApplicationContext(),"تنظیمات",Toast.LENGTH_SHORT).show();
                break;

            case R.id.about_me :
                Intent aboutMeActivity = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(aboutMeActivity);
                break;

            case R.id.exit :
                Preferences preferences = new Preferences(this);
                preferences.exitAccount();
                Intent loginOrSignUpActivity = new Intent(MainActivity.this, LoginOrSignUpActivity.class);
                startActivity(loginOrSignUpActivity);
                finish();
                break;
        }
        mainDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    private void runWalletAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.WalletAlertDialog);
        builder.setView(R.layout.wallet_alert_dialog)
                .setCancelable(false);

        final AlertDialog dialog = builder.create();
        dialog.show();

        tvCredit = dialog.findViewById(R.id.tv_credit);
        etIncreaseCredit = dialog.findViewById(R.id.et_increase_credit);
        btnCloseWallet = dialog.findViewById(R.id.btn_close_wallet);
        btnIncreaseCredit = dialog.findViewById(R.id.btn_increase_credit);

        DecimalFormat d = new DecimalFormat("###,###,###");
        String money = "اعتبار موجود : " + String.valueOf(d.format(inventory))+ " " +  "ریال";
        tvCredit.setText(money);

        btnIncreaseCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etIncreaseCredit.getText().toString().isEmpty()) {
                    etIncreaseCredit.setError("مبلغ مورد نظر را وارد کنید.");
                } else {
                    Preferences p = new Preferences(MainActivity.this);
                    PersianCalendar now = new PersianCalendar();
                    int pId = p.getPassengerId();
                    int m = now.getPersianMonth() + 1;
                    String nowDate = now.getPersianYear() + "/" + m + "/" + now.getPersianDay();
                    String nowTime = now.getTime().getHours() + ":" + now.getTime().getMinutes();
                    String moneyIncrease = etIncreaseCredit.getText().toString();
                    GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
                    Call<ResponseModel> call = api.increaseCredit(Integer.parseInt(moneyIncrease),pId,nowDate,nowTime);
                    call.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            switch (response.body().getResponse()) {
                                case "SUCCESS":
                                    Toast.makeText(getApplicationContext(), "اعتبار حساب شما افزایش یافت.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR":
                                    Toast.makeText(getApplicationContext(), "افزایش اعتبار انجام نشد. لطفا دوباره امتحان کنید!!", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Log.i(TAG, "onIncreaseResponse: " + response.code() + " " + response.message());
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

                    dialog.dismiss();

                }
            }
        });

        btnCloseWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void getInventoryFromServer() {
        Preferences preferences = new Preferences(this);
        int pId = preferences.getPassengerId();
        GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
        Call<PassengerFinancialAccountModel> call = api.getInventory(pId);
        call.enqueue(new Callback<PassengerFinancialAccountModel>() {
            @Override
            public void onResponse(Call<PassengerFinancialAccountModel> call, Response<PassengerFinancialAccountModel> response) {
                if (response.isSuccessful()) {
                    PassengerFinancialAccountModel model = response.body();
                    inventory = model.getInventory();
                } else {
                    Log.i(TAG, "onReserveTripResponse: " + response.code() + " " + response.message());
                    Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PassengerFinancialAccountModel> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigation) {
            Intent navigationActivity = new Intent(MainActivity.this,NavigationActivity.class);
            startActivity(navigationActivity);
        }
        return super.onOptionsItemSelected(item);
    }
}
