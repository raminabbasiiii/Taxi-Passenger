package com.example.ramin.passenger.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionModel {

    @SerializedName("money")
    @Expose
    private int money;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("time")
    @Expose
    private String time;

    public int getMoney() {
        return money;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
