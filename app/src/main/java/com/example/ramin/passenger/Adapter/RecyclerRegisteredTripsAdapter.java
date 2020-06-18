package com.example.ramin.passenger.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ramin.passenger.Model.RegisteredTripsModel;
import com.example.ramin.passenger.Model.ResponseModel;
import com.example.ramin.passenger.NavigationActivity;
import com.example.ramin.passenger.Network.GetPassengerData;
import com.example.ramin.passenger.Network.RetrofitPassengerInstance;
import com.example.ramin.passenger.Preferences;
import com.example.ramin.passenger.R;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerRegisteredTripsAdapter extends RecyclerView.Adapter<RecyclerRegisteredTripsAdapter.MyViewHolder>{


    private List<RegisteredTripsModel> list;
    private Context context;
    private static final String TAG ="TAG";

    public RecyclerRegisteredTripsAdapter(List<RegisteredTripsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.registered_trips_recycler_list_item,parent,false);
        return new MyViewHolder(aView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final RegisteredTripsModel model = list.get(position);

        String name = model.getDriverName() + " " + model.getDriverFamily();
        String carModel = model.getDriverCarModel() + " " + model.getDriverCarColor();
        String dateTime = model.getDateOfMovement() + "     " + model.getTimeOfMovement();
        String distance = String.valueOf(model.getDistance()) + " " + "کیلومتر";
        int money = model.getCostOfPayment();
        DecimalFormat d = new DecimalFormat("###,###,###");
        String rial = String.valueOf(d.format(money)) + " " + "ریال";
        String tId = String.valueOf(model.getTripId());

        holder.tvDriverName.setText(name);
        holder.tvCarModel.setText(carModel);
        holder.tvOrigin.setText(model.getOrigin());
        holder.tvDestination.setText(model.getDestination());
        holder.tvDate.setText(dateTime);
        holder.tvDistance.setText(distance);
        holder.tvMoney.setText(rial);
        holder.tvTripId.setText(tId);
        holder.tvMobile.setText(model.getDriverMobile());
        Glide.with(context).load(model.getDriverImage()).into(holder.driverPicture);

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:" + model.getDriverMobile()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(call);
            }
        });

        holder.btnShowPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigationActivity = new Intent(context, NavigationActivity.class);
                navigationActivity.putExtra("tripId",model.getTripId());
                context.startActivity(navigationActivity);
            }
        });

        holder.btnCancelTripRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btnPositiveCancelTrip,btnNegativeCancelTrip;

                AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.CancelAlertDialog);
                builder.setView(R.layout.cancel_alert_dialog)
                        .setCancelable(true);

                final AlertDialog dialog = builder.create();
                dialog.show();

                btnPositiveCancelTrip = dialog.findViewById(R.id.btn_positive_cancel_trip);
                btnNegativeCancelTrip = dialog.findViewById(R.id.btn_negative_cancel_trip);

                btnPositiveCancelTrip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tId = list.get(holder.getAdapterPosition()).getTripId();
                        int sId = list.get(holder.getAdapterPosition()).getSubId();
                        Preferences p = new Preferences(context);
                        int pId = p.getPassengerId();
                        GetPassengerData api = RetrofitPassengerInstance.getRetrofitPassenger().create(GetPassengerData.class);
                        Call<ResponseModel> call = api.cancelRegisteredTrip(tId,sId,pId);
                        call.enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                switch (response.body().getResponse()) {
                                    case "DELETED":
                                        Toast.makeText(context, "سفر لغو شد!", Toast.LENGTH_LONG).show();

                                        list.remove(holder.getAdapterPosition());
                                        notifyDataSetChanged();
                                        break;
                                    case "FAILED":
                                        Toast.makeText(context, "سفر لغو نشد. دوباره امتحان کتید!", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                        break;
                                    default:
                                        Log.i(TAG, "onCancelResponse: " + response.code() + " " + response.message());
                                        Toast.makeText(context, "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                Log.i(TAG, "onFailure: " + t.getMessage());
                                Toast.makeText(context, "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();

                    }
                });

                btnNegativeCancelTrip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"سفر لغو نشد!!!",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateRegisteredTripList(List<RegisteredTripsModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageButton btnCancelTripRegistered,btnShowPlace,btnCall;
        private TextView tvDriverName,tvCarModel,tvOrigin,tvDestination,tvDate,tvDistance,tvMoney,tvTripId,tvMobile;
        private CircleImageView driverPicture;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvDriverName = itemView.findViewById(R.id.tv_driver_name_registered);
            tvCarModel = itemView.findViewById(R.id.tv_car_model_registered);
            tvOrigin = itemView.findViewById(R.id.tv_origin_registered);
            tvDestination = itemView.findViewById(R.id.tv_destination_registered);
            tvDate = itemView.findViewById(R.id.tv_date_movement_registered);
            tvDistance = itemView.findViewById(R.id.tv_number_distance_registered);
            tvMoney = itemView.findViewById(R.id.tv_money_registered);
            tvTripId = itemView.findViewById(R.id.tv_trip_id_registered);
            tvMobile = itemView.findViewById(R.id.tv_mobile_registered);

            driverPicture = itemView.findViewById(R.id.driver_picture_registered);

            btnShowPlace = itemView.findViewById(R.id.img_show_place_registered);
            btnCall = itemView.findViewById(R.id.btn_call_registered);
            btnCancelTripRegistered = itemView.findViewById(R.id.btn_cancel_trip_registered);
        }
    }

   /* private void runCancelAlert() {

        Button btnPositiveCancelTrip,btnNegativeCancelTrip;

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.CancelAlertDialog);
        builder.setView(R.layout.cancel_alert_dialog)
                .setCancelable(true);

        final AlertDialog dialog = builder.create();
        dialog.show();

        btnPositiveCancelTrip = dialog.findViewById(R.id.btn_positive_cancel_trip);
        btnNegativeCancelTrip = dialog.findViewById(R.id.btn_negative_cancel_trip);

        btnPositiveCancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnNegativeCancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"سفر لغو نشد!!!",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }*/
}
