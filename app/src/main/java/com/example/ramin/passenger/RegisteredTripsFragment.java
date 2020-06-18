package com.example.ramin.passenger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ramin.passenger.Adapter.RecyclerRegisteredTripsAdapter;
import com.example.ramin.passenger.Model.RegisteredTripsModel;
import com.example.ramin.passenger.Network.GetPassengerData;
import com.example.ramin.passenger.Network.RetrofitPassengerInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisteredTripsFragment extends Fragment {

    View root;
    RecyclerView registeredTripsRecycler;
    RecyclerRegisteredTripsAdapter adapter;
    List<RegisteredTripsModel> modelList = new ArrayList<>();
    private static final String TAG ="TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_registered_trips, container, false);

        setRecyclerView();
        getRegisteredTripFromServer();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setRecyclerView() {
        registeredTripsRecycler = root.findViewById(R.id.registered_trips_recycler);
        adapter = new RecyclerRegisteredTripsAdapter(modelList,getContext());
        RecyclerView.LayoutManager regLayoutManager = new LinearLayoutManager(getContext());
        registeredTripsRecycler.setLayoutManager(regLayoutManager);
        registeredTripsRecycler.setItemAnimator(new DefaultItemAnimator());
        registeredTripsRecycler.setHasFixedSize(true);
        registeredTripsRecycler.setAdapter(adapter);

    }

    private void getRegisteredTripFromServer() {
        if (getActivity() != null) {
            Preferences preferences = new Preferences(getActivity());
            int pId = preferences.getPassengerId();
            GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
            Call<List<RegisteredTripsModel>> call = api.getRegisteredTrips(pId);
            call.enqueue(new Callback<List<RegisteredTripsModel>>() {
                @Override
                public void onResponse(Call<List<RegisteredTripsModel>> call, Response<List<RegisteredTripsModel>> response) {
                    if (response.isSuccessful()) {
                        modelList = response.body();
                        adapter.updateRegisteredTripList(modelList);
                    } else {
                        Log.i(TAG, "onRegisteredTripsResponse: " + response.code() + " " + response.message());
                        Toast.makeText(getActivity(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<RegisteredTripsModel>> call, Throwable t) {
                    Log.i(TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(getActivity(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
