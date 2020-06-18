package com.example.ramin.passenger.Activity;

import android.content.Context;
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

import com.example.ramin.passenger.Network.GetPassengerData;
import com.example.ramin.passenger.Network.RetrofitPassengerInstance;
import com.example.ramin.passenger.Preferences;
import com.example.ramin.passenger.R;
import com.example.ramin.passenger.Adapter.RecyclerTransactionAdapter;
import com.example.ramin.passenger.Model.TransactionModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TransactionActivity extends AppCompatActivity {

    Toolbar transactionToolbar;
    RecyclerView transactionRecycler;
    RecyclerTransactionAdapter adapter;
    List<TransactionModel> modelList = new ArrayList<>();
    TextView transactionToolbarTitle;
    private static final String TAG ="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setToolbar();
        setRecyclerView();
        getTransactionFromServer();
    }

    private void setToolbar() {
        transactionToolbar = findViewById(R.id.transaction_toolbar);
        transactionToolbarTitle =transactionToolbar.findViewById(R.id.transaction_toolbar_title);
        setSupportActionBar(transactionToolbar);
        transactionToolbarTitle.setText(R.string.transaction_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setRecyclerView() {
        transactionRecycler = findViewById(R.id.transaction_recycler);
        adapter = new RecyclerTransactionAdapter(modelList,getApplicationContext());
        RecyclerView.LayoutManager regLayoutManager = new LinearLayoutManager(getApplicationContext());
        transactionRecycler.setLayoutManager(regLayoutManager);
        transactionRecycler.setItemAnimator(new DefaultItemAnimator());
        transactionRecycler.setHasFixedSize(true);
        transactionRecycler.setAdapter(adapter);
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

    private void getTransactionFromServer() {
        Preferences p = new Preferences(TransactionActivity.this);
        int pId = p.getPassengerId();
        final GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
        Call<List<TransactionModel>> call = api.getTransaction(pId);
        call.enqueue(new Callback<List<TransactionModel>>() {
            @Override
            public void onResponse(Call<List<TransactionModel>> call, Response<List<TransactionModel>> response) {
                if (response.isSuccessful()) {
                    modelList = response.body();
                    adapter.updateTransactionList(modelList);
                } else {
                    Log.i(TAG, "onTransactionResponse: " + response.code() + " " + response.message());
                    Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TransactionModel>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
