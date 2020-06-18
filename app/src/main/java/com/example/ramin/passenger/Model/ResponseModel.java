package com.example.ramin.passenger.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseModel {

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("chair_reserved_count")
    @Expose
    private int chairReservedCount;

    public String getResponse() {
        return response;
    }

    public int getChairReservedCount() {
        return chairReservedCount;
    }
}
