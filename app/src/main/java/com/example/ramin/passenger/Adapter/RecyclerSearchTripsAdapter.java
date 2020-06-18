package com.example.ramin.passenger.Adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramin.passenger.ClickListener;
import com.example.ramin.passenger.Model.SearchTripsModel;
import com.example.ramin.passenger.Network.GetPassengerData;
import com.example.ramin.passenger.Network.RetrofitPassengerInstance;
import com.example.ramin.passenger.R;
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

public class RecyclerSearchTripsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int LAYOUT_ONE = 0;
    private static final int LAYOUT_TWO = 1;
    private static final String TAG ="TAG";
    private static ClickListener clickListener;
    private List<SearchTripsModel> list ;
    private Context context;

    public RecyclerSearchTripsAdapter(List<SearchTripsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    /*@Override
    public int getItemViewType(int position) {
        if(position==0)
            return LAYOUT_ONE;
        else
            return LAYOUT_TWO;
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        //RecyclerView.ViewHolder viewHolder;

        /*if(viewType==LAYOUT_ONE)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_trips_recycler_list_item_one,parent,false);
            viewHolder = new MyViewHolderOne(view);
        }*/
       // else
        //{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_trips_recycler_list_item_two,parent,false);
           // viewHolder= new MyViewHolderTwo(view);
        return new MyViewHolderTwo(view);
        //}

        //return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        /*if (holder.getItemViewType() == LAYOUT_ONE) {

            final MyViewHolderOne myViewHolderOne = (MyViewHolderOne) holder;

            myViewHolderOne.originImgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            myViewHolderOne.destinationImgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            myViewHolderOne.dateImgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = ((AppCompatActivity)context).getFragmentManager();
                    PersianCalendar now = new PersianCalendar();
                    DatePickerDialog pickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            String persianDate = "" + year + "/" + monthOfYear + "/" + dayOfMonth;
                            myViewHolderOne.etDate.setText(persianDate);
                        }
                    }, now.getPersianYear(),now.getPersianMonth(),now.getPersianDay());
                        pickerDialog.show(manager, "tag");
                }
            });
            myViewHolderOne.timeImgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = ((AppCompatActivity)context).getFragmentManager();
                    PersianCalendar now = new PersianCalendar();
                    TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                            String time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                            myViewHolderOne.etTime.setText(time);
                        }
                    }, now.getTime().getHours(), now.getTime().getMinutes(), true);
                        timePickerDialog.show(manager,"tag");
                }
            });
            myViewHolderOne.btnSearchTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String origin = myViewHolderOne.etOrigin.getText().toString();
                    String destination = myViewHolderOne.etDestination.getText().toString();
                    String date = myViewHolderOne.etDate.getText().toString();
                    String time = myViewHolderOne.etTime.getText().toString();
                    GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
                    Call<List<SearchTripsModel>> call = api.getSearchTrips(origin,destination,date,time);
                    call.enqueue(new Callback<List<SearchTripsModel>>() {
                        @Override
                        public void onResponse(Call<List<SearchTripsModel>> call, Response<List<SearchTripsModel>> response) {
                            if (response.isSuccessful()) {
                                list = response.body();
                            } else {
                                Log.i(TAG, "onSearchTripsResponse: " + response.code() + " " + response.message());
                                Toast.makeText(context, "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SearchTripsModel>> call, Throwable t) {
                            Log.i(TAG, "onFailure: " + t.getMessage());
                            Toast.makeText(context, "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else
        {*/
            final MyViewHolderTwo myViewHolderTwo = (MyViewHolderTwo) holder;

            final SearchTripsModel model = list.get(position);
            int sId = model.getSubId();

            GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
            Call<SearchTripsModel> call = api.getEmptyChairCount(model.getTripId(),sId);
            call.enqueue(new Callback<SearchTripsModel>() {
                @Override
                public void onResponse(Call<SearchTripsModel> call, Response<SearchTripsModel> response) {
                    if (response.isSuccessful()) {
                        SearchTripsModel model = response.body();
                        myViewHolderTwo.emptyChair.setText(String.valueOf(model.getEmptyChairCount()));
                    }else {
                        Log.i(TAG, "onTripsResponse: " + response.code() + " " + response.message());
                        Toast.makeText(context, "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SearchTripsModel> call, Throwable t) {
                    Log.i(TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(context, "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                }
            });

            int price = model.getPrice();
            //int emptyChairCount = model.getEmptyChairCount();
            DecimalFormat d = new DecimalFormat("###,###,###");
            String rial = String.valueOf(d.format(price)) + " " + "ریال";
            String time = model.getDateOfMovement() + "     " + model.getTimeOfMovement();

            myViewHolderTwo.origin.setText(model.getOrigin());
            myViewHolderTwo.destination.setText(model.getDestination());
            myViewHolderTwo.money.setText(rial);
            //myViewHolderTwo.emptyChair.setText(String.valueOf(emptyChairCount));
            myViewHolderTwo.time.setText(time);
       // }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateSearchTripsList(List<SearchTripsModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    /*public class MyViewHolderOne extends RecyclerView.ViewHolder{

        private TextInputLayout etOriginLayout,etDestinationLayout,etDateLayout,etTimeLayout;
        private EditText etOrigin,etDestination,etDate,etTime;
        private ImageButton originImgButton,destinationImgButton,dateImgButton,timeImgButton;
        private Button btnSearchTrip;

        public MyViewHolderOne(View itemView) {
            super(itemView);

            etOrigin = itemView.findViewById(R.id.et_origin);
            etOriginLayout = itemView.findViewById(R.id.et_origin_layout);

            etDestination = itemView.findViewById(R.id.et_destination);
            etDestinationLayout = itemView.findViewById(R.id.et_destination_layout);

            etDate = itemView.findViewById(R.id.et_date);
            etDateLayout = itemView.findViewById(R.id.et_date_layout);

            etTime = itemView.findViewById(R.id.et_time);
            etTimeLayout = itemView.findViewById(R.id.et_time_layout);

            originImgButton = itemView.findViewById(R.id.origin_imgButton);
            destinationImgButton = itemView.findViewById(R.id.destination_imgButton);
            dateImgButton = itemView.findViewById(R.id.date_imgButton);
            timeImgButton = itemView.findViewById(R.id.time_imgButton);

            btnSearchTrip = itemView.findViewById(R.id.btn_search_trips);
        }
    }*/

    public class MyViewHolderTwo extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView origin;
        private TextView destination;
        private TextView money;
        private TextView emptyChair;
        private TextView time;

        public MyViewHolderTwo(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            origin = itemView.findViewById(R.id.tv_origin);
            destination = itemView.findViewById(R.id.tv_destination);
            money = itemView.findViewById(R.id.tv_money);
            emptyChair = itemView.findViewById(R.id.tv_empty_chair);
            time = itemView.findViewById(R.id.tv_time_movement);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v,getAdapterPosition());
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        RecyclerSearchTripsAdapter.clickListener = clickListener;
    }
}
