package com.example.ramin.passenger.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramin.passenger.ClickListener;
import com.example.ramin.passenger.Network.GetPassengerData;
import com.example.ramin.passenger.Network.RetrofitPassengerInstance;
import com.example.ramin.passenger.Preferences;
import com.example.ramin.passenger.R;
import com.example.ramin.passenger.Adapter.RecyclerTripsAdapter;
import com.example.ramin.passenger.Model.TripsModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TripsActivity extends AppCompatActivity {

    Toolbar tripsToolbar;
    RecyclerView tripsRecycler;
    RecyclerTripsAdapter adapter;
    List<TripsModel> modelList = new ArrayList<>();
    TextView tripsToolbarTitle;
    private static final String TAG ="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setToolbar();
        setRecyclerView();
        getTripsListFromServer();
    }

    private void setToolbar() {
        tripsToolbar = findViewById(R.id.trip_toolbar);
        tripsToolbarTitle = tripsToolbar.findViewById(R.id.trips_toolbar_title);
        setSupportActionBar(tripsToolbar);
        tripsToolbarTitle.setText(R.string.trips_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setRecyclerView() {
        tripsRecycler = findViewById(R.id.trips_recycler);
        adapter = new RecyclerTripsAdapter(modelList,getApplicationContext());
        RecyclerView.LayoutManager regLayoutManager = new LinearLayoutManager(getApplicationContext());
        tripsRecycler.setLayoutManager(regLayoutManager);
        tripsRecycler.setItemAnimator(new DefaultItemAnimator());
        tripsRecycler.setHasFixedSize(true);
        tripsRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                int id = modelList.get(position).getTripId();
                int subId = modelList.get(position).getSubId();
                Bundle sendId = new Bundle();
                sendId.putInt("tripId",id);
                sendId.putInt("subId",subId);
                Intent tripDetailActivity = new Intent(TripsActivity.this,TripDetailsActivity.class);
                tripDetailActivity.putExtras(sendId);
                startActivity(tripDetailActivity);
            }
        });
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

    private void getTripsListFromServer() {
        Preferences p = new Preferences(TripsActivity.this);
        int pId = p.getPassengerId();

        GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
        Call<List<TripsModel>> call = api.getDoTrips(pId);
        call.enqueue(new Callback<List<TripsModel>>() {
            @Override
            public void onResponse(Call<List<TripsModel>> call, Response<List<TripsModel>> response) {
                if (response.isSuccessful()) {
                    modelList = response.body();
                    adapter.updateTripsList(modelList);
                }else {
                    Log.i(TAG, "onDoTripsResponse: " + response.code() + " " + response.message());
                    Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TripsModel>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
