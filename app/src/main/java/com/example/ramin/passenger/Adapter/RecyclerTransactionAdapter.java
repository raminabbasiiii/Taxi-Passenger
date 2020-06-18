package com.example.ramin.passenger.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ramin.passenger.Model.TransactionModel;
import com.example.ramin.passenger.R;

import java.text.DecimalFormat;
import java.util.List;

public class RecyclerTransactionAdapter extends RecyclerView.Adapter<RecyclerTransactionAdapter.MyViewHolder>{

    //private static final int LAYOUT_INCREASE = 0;
    //private static final int LAYOUT_DECREASE = 1;
    private List<TransactionModel> list;
    private Context context;

    public RecyclerTransactionAdapter(List<TransactionModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    /*@Override
    public int getItemViewType(int position) {
        if(position == 0)
            return LAYOUT_INCREASE;
        else
            return LAYOUT_DECREASE;
    }*/

    /*@Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;

        if (viewType == LAYOUT_INCREASE)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_recycler_list_item_increase,parent,false);
            viewHolder = new MyViewHolderIncrease(view);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_recycler_list_item_decrease,parent,false);
            viewHolder = new MyViewHolderDecrease(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        TransactionModel model = list.get(position);

        if (holder.getItemViewType() == LAYOUT_INCREASE)
        {
            MyViewHolderIncrease myViewHolderIncrease = (MyViewHolderIncrease) holder;

            int price = model.getAmountOfCreditIncrease();
            DecimalFormat d = new DecimalFormat("###,###,###");
            String rial = String.valueOf(d.format(price)) + " " + "ریال";

            myViewHolderIncrease.tvMoneyIncrease.setText(rial);
            myViewHolderIncrease.tvDateTimeIncrease.setText(model.getDateOfCreditIncrease());
        }
        else
        {
            MyViewHolderDecrease myViewHolderDecrease = (MyViewHolderDecrease) holder;

            int price = model.getAmountOfPayment();
            DecimalFormat d = new DecimalFormat("###,###,###");
            String rial = String.valueOf(d.format(price)) + " " + "ریال";

            myViewHolderDecrease.tvMoneyDecrease.setText(rial);
            myViewHolderDecrease.tvDateTimeDecrease.setText(model.getDateOfPayment());
        }

    }*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_recycler_list_item_increase,parent,false);
        return new MyViewHolder(aView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TransactionModel model = list.get(position);

        int price = model.getMoney();
        String money = String.valueOf(price);
        DecimalFormat d = new DecimalFormat("###,###,###");
        String rial = String.valueOf(d.format(price)) + " " + "ریال";
        String dateTime = model.getDate() + "       " + model.getTime();

        if (money.contains("-")) {
            holder.tvIncrease.setBackgroundColor(Color.RED);
            holder.tvIncrease.setText(R.string.tv_decrease);
            holder.tvDateTimeDecrease.setText(R.string.tv_date_decrease);
            holder.tvMoneyDecrease.setText(R.string.tv_cost_decrease);
            String dMoney = money.replace("-","");
            DecimalFormat dd = new DecimalFormat("###,###,###");
            String riall = String.valueOf(dd.format(Integer.parseInt(dMoney))) + " " + "ریال";
            holder.tvMoneyIncrease.setText(riall);
            holder.tvDateTimeIncrease.setText(dateTime);

        } else {
            holder.tvMoneyIncrease.setText(rial);
            holder.tvDateTimeIncrease.setText(dateTime);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateTransactionList(List<TransactionModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDateTimeIncrease,tvMoneyIncrease,tvIncrease,tvDateTimeDecrease,tvMoneyDecrease;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvDateTimeIncrease = itemView.findViewById(R.id.tv_date_time_increase);
            tvMoneyIncrease = itemView.findViewById(R.id.tv_money_increase);
            tvIncrease= itemView.findViewById(R.id.tv_increase);
            tvDateTimeDecrease = itemView.findViewById(R.id.tv_date_increase);
            tvMoneyDecrease = itemView.findViewById(R.id.tv_cost_increase);
        }
    }

    /*public class MyViewHolderIncrease extends RecyclerView.ViewHolder {

        private TextView tvDateTimeIncrease,tvMoneyIncrease;

        public MyViewHolderIncrease(View itemView) {
            super(itemView);

            tvDateTimeIncrease = itemView.findViewById(R.id.tv_date_time_increase);
            tvMoneyIncrease = itemView.findViewById(R.id.tv_money_increase);
        }
    }

    public class MyViewHolderDecrease extends RecyclerView.ViewHolder {

        private TextView tvDateTimeDecrease,tvMoneyDecrease;

        public MyViewHolderDecrease(View itemView) {
            super(itemView);

            tvDateTimeDecrease = itemView.findViewById(R.id.tv_date_time_decrease);
            tvMoneyDecrease = itemView.findViewById(R.id.tv_money_decrease);
        }
    }*/
}
