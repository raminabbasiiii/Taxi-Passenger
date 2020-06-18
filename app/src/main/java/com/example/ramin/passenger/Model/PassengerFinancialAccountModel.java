package com.example.ramin.passenger.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassengerFinancialAccountModel {

    @SerializedName("inventory")
    @Expose
    private int inventory;

    public int getInventory() {
        return inventory;
    }
}
