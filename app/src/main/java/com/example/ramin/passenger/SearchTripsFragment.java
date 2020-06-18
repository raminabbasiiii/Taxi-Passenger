package com.example.ramin.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ramin.passenger.Activity.ReserveTripActivity;
import com.example.ramin.passenger.Adapter.RecyclerSearchTripsAdapter;
import com.example.ramin.passenger.Model.SearchTripsModel;
import com.example.ramin.passenger.Network.GetPassengerData;
import com.example.ramin.passenger.Network.RetrofitPassengerInstance;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class SearchTripsFragment extends Fragment implements  View.OnClickListener,TimePickerDialog.OnTimeSetListener {

    View root;
    RecyclerView searchTripsRecycler;
    RecyclerSearchTripsAdapter adapter;
    List<SearchTripsModel> modelList = new ArrayList<>();
    TextInputLayout etOriginLayout,etDestinationLayout,etDateLayout,etTimeLayout;
    EditText etOrigin,etDestination,etDate,etTime;
    ImageButton originImgButton,destinationImgButton,dateImgButton,timeImgButton;
    Button btnSearchTrip;
    String origin,destination;
    private static final String TAG ="TAG";
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_search_trips, container, false);

        setRecyclerView();
        setFab();
        getAllTripsFromServer();
        //findViews();
        //getData();
        return root;
    }

    private void setRecyclerView() {
        searchTripsRecycler = root.findViewById(R.id.search_trips_recycler);
        adapter = new RecyclerSearchTripsAdapter(modelList,getContext());
        RecyclerView.LayoutManager regLayoutManager = new LinearLayoutManager(getContext());
        searchTripsRecycler.setLayoutManager(regLayoutManager);
        searchTripsRecycler.setItemAnimator(new DefaultItemAnimator());
        searchTripsRecycler.setHasFixedSize(true);
        searchTripsRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                int id = modelList.get(position).getTripId();
                int subId = modelList.get(position).getSubId();

                Bundle sendId = new Bundle();
                sendId.putInt("tripId",id);
                sendId.putInt("subId",subId);
                Intent reserveTripActivity = new Intent(getContext(),ReserveTripActivity.class);
                reserveTripActivity.putExtras(sendId);
                startActivity(reserveTripActivity);
            }
        });
    }

    private void setFab() {
        fab = root.findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runSearchTripsAlert();
            }
        });
    }
    /*private void findViews() {
        etOrigin = root.findViewById(R.id.et_origin);
        etOriginLayout = root.findViewById(R.id.et_origin_layout);


        etDestination = root.findViewById(R.id.et_destination);
        etDestinationLayout = root.findViewById(R.id.et_destination_layout);

        etDate = root.findViewById(R.id.et_date);
        etDateLayout = root.findViewById(R.id.et_date_layout);

        etTime = root.findViewById(R.id.et_time);
        etTimeLayout = root.findViewById(R.id.et_time_layout);

        originImgButton = root.findViewById(R.id.origin_imgButton);
        originImgButton.setOnClickListener(this);
        destinationImgButton = root.findViewById(R.id.destination_imgButton);
        destinationImgButton.setOnClickListener(this);
        dateImgButton = root.findViewById(R.id.date_imgButton);
        dateImgButton.setOnClickListener(this);
        timeImgButton = root.findViewById(R.id.time_imgButton);
        timeImgButton.setOnClickListener(this);

        btnSearchTrip = root.findViewById(R.id.btn_search_trips);
        btnSearchTrip.setOnClickListener(this);
    }*/

    /*private void getData() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            origin = mainActivity.getGetOrigin();
            destination = mainActivity.getGetDestination();

        }
    }*/

    private void runSearchTripsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.SearchTripsAlertDialog);
        builder.setView(R.layout.search_trips_alert_dialog)
                .setCancelable(true);

        final AlertDialog dialog = builder.create();
        dialog.show();

        etOrigin = dialog.findViewById(R.id.et_origin);
        etOriginLayout = dialog.findViewById(R.id.et_origin_layout);
        etOrigin.setOnClickListener(this);

        etDestination = dialog.findViewById(R.id.et_destination);
        etDestinationLayout = dialog.findViewById(R.id.et_destination_layout);
        etDestination.setOnClickListener(this);

        etDate = dialog.findViewById(R.id.et_date);
        etDateLayout = dialog.findViewById(R.id.et_date_layout);
        etDate.setOnClickListener(this);

        /*etTime = dialog.findViewById(R.id.et_time);
        etTimeLayout = dialog.findViewById(R.id.et_time_layout);
        etTime.setOnClickListener(this);*/



        btnSearchTrip = dialog.findViewById(R.id.btn_search_trips);


        btnSearchTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOrigin.getText().toString().isEmpty() || etDestination.getText().toString().isEmpty() || etDate.getText().toString().isEmpty()) {
                    /*if (etOrigin.getText().toString().isEmpty()) {
                        etOriginLayout.setError("مبدا را وارد کنید.");
                    } else {
                        etOriginLayout.setErrorEnabled(false);
                    }
                    if (etDestination.getText().toString().isEmpty()) {
                        etDestinationLayout.setError("مقصد را وارد کنید.");
                    } else {
                        etDestinationLayout.setErrorEnabled(false);
                    }
                    if (etDate.getText().toString().isEmpty()) {
                        etDateLayout.setError("تاریخ را وارد کنید.");
                    } else {
                        etDateLayout.setErrorEnabled(false);
                    }
                    if (etTime.getText().toString().isEmpty()) {
                        etTimeLayout.setError("تاریخ را وارد کنید.");
                    } else {
                        etTimeLayout.setErrorEnabled(false);
                    }

                    etOrigin.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (etOrigin.getText().toString().isEmpty()) {
                                etOriginLayout.setError("مبدا را وارد کنید.");
                            } else {
                                etOriginLayout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    etDestination.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (etDestination.getText().toString().isEmpty()) {
                                etDestinationLayout.setError("مقصد را وارد کنید.");
                            } else {
                                etDestinationLayout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    etDate.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (etDate.getText().toString().isEmpty()) {
                                etDateLayout.setError("تاریخ را وارد کنید.");
                            } else {
                                etDateLayout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    etTime.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (etTime.getText().toString().isEmpty()) {
                                etTimeLayout.setError("تاریخ را وارد کنید.");
                            } else {
                                etTimeLayout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });*/
                    Toast.makeText(getContext(),"فیلد های مورد نظر را پر کنید ",Toast.LENGTH_LONG).show();
                } else {
                    String origin = etOrigin.getText().toString();
                    String destination = etDestination.getText().toString();
                    String date = etDate.getText().toString();
                    //String time = etTime.getText().toString();
                    final GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
                    Call<List<SearchTripsModel>> call = api.getSearchTrips(origin, destination, date);
                    call.enqueue(new Callback<List<SearchTripsModel>>() {
                        @Override
                        public void onResponse(Call<List<SearchTripsModel>> call, Response<List<SearchTripsModel>> response) {
                            if (response.isSuccessful()) {
                                modelList = response.body();
                                if (modelList.get(0).getResponse().equals("FAILED")) {

                                    Toast.makeText(getContext(),"سفر موجود نیست!!!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    searchTripsRecycler.setVisibility(View.GONE);
                                } else {
                                    searchTripsRecycler.setVisibility(View.VISIBLE);
                                    adapter.updateSearchTripsList(modelList);
                                    dialog.dismiss();
                                }
                            } else {
                                Log.i(TAG, "onSearchTripsResponse: " + response.code() + " " + response.message());

                                Toast.makeText(getContext(),"خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SearchTripsModel>> call, Throwable t) {
                            Log.i(TAG, "onFailure: " + t.getMessage());
                            Toast.makeText(getContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.et_date :
                persianDatePicker();
                break;

            /*case R.id.et_time :
                PersianCalendar now = new PersianCalendar();
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this,now.getTime().getHours(),now.getTime().getMinutes(),true );
                if (getActivity() != null)
                    timePickerDialog.show(getActivity().getFragmentManager(),"tag");
                break;*/

            case R.id.et_origin :
                Intent navigationActivity1 = new Intent(getContext(),NavigationActivity.class);
                startActivityForResult(navigationActivity1,1);
                /*if (getActivity() != null)
                    getActivity().finish();*/
                break;

            case R.id.et_destination :
                Intent navigationActivity2 = new Intent(getContext(),NavigationActivity.class);
                startActivityForResult(navigationActivity2,1);
                /*if (getActivity() != null)
                    getActivity().finish();*/
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                origin = data.getStringExtra("origin");
                destination =data.getStringExtra("destination");
                etOrigin.setText(origin);
                etDestination.setText(destination);
            }
        }
    }

    private void persianDatePicker(){
        PersianCalendar now = new PersianCalendar();
        DatePickerDialog pickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                //Toast.makeText(getApplicationContext(),"" + year + "/" + monthOfYear + "/" + dayOfMonth,Toast.LENGTH_LONG).show();
                int m = monthOfYear+1;
                String persianDate = "" + year + "/" + m + "/" + dayOfMonth;
                etDate.setText(persianDate);
            }
        }, now.getPersianYear(),now.getPersianMonth(),now.getPersianDay());

        if (getActivity() != null) {
            pickerDialog.show(getActivity().getFragmentManager(), "tag");
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
        etTime.setText(time);
    }

    private void getAllTripsFromServer() {
        GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
        Call<List<SearchTripsModel>> call = api.getAllTrips();
        call.enqueue(new Callback<List<SearchTripsModel>>() {
            @Override
            public void onResponse(Call<List<SearchTripsModel>> call, Response<List<SearchTripsModel>> response) {
                if (response.isSuccessful()) {
                    modelList = response.body();
                    adapter.updateSearchTripsList(modelList);
                }else {
                    Log.i(TAG, "onSearchTripsResponse: " + response.code() + " " + response.message());
                    Toast.makeText(getContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SearchTripsModel>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
