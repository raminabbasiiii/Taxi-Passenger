package com.example.ramin.passenger.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ramin.passenger.ClickListener;
import com.example.ramin.passenger.R;
import com.example.ramin.passenger.Model.TripsModel;

import java.util.List;

public class RecyclerTripsAdapter extends RecyclerView.Adapter<RecyclerTripsAdapter.MyViewHolder>{

    private List<TripsModel> regList;
    private Context context;
    private static ClickListener clickListener;

    public RecyclerTripsAdapter(List<TripsModel> regList, Context context) {
        this.regList = regList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trips_recycler_list_item,parent,false);
        return new MyViewHolder(aView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TripsModel model = regList.get(position);

        String dateTime = model.getDateOfMovement() + "     " + model.getTimeOfMovement();
        holder.dateTimeDoing.setText(dateTime);
        holder.destination.setText(model.getDestination());
        holder.origin.setText(model.getOrigin());
    }

    @Override
    public int getItemCount() {
        return regList.size();
    }

    public void updateTripsList(List<TripsModel> list) {
        this.regList = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView origin,destination,dateTimeDoing;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            origin = itemView.findViewById(R.id.tv_origin_trips);
            destination = itemView.findViewById(R.id.tv_destination_trips);
            dateTimeDoing = itemView.findViewById(R.id.tv_date_time_doing_trips);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v,getAdapterPosition());
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        RecyclerTripsAdapter.clickListener = clickListener;
    }
}
