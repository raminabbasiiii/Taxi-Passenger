package com.example.ramin.passenger.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassengerModel {

    @SerializedName("passenger_id")
    @Expose
    private int passengerId;

    @SerializedName("passenger_name")
    @Expose
    private String passengerName;

    @SerializedName("passenger_family")
    @Expose
    private String passengerFamily;

    @SerializedName("passenger_sexuality")
    @Expose
    private String passengerSexuality;

    @SerializedName("passenger_mail")
    @Expose
    private String passengerMail;

    @SerializedName("passenger_mobile")
    @Expose
    private String passengerMobile;

    @SerializedName("passenger_password")
    @Expose
    private String passengerPassword;

    @SerializedName("response")
    @Expose
    private String response;

    public int getPassengerId() {
        return passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getPassengerFamily() {
        return passengerFamily;
    }

    public String getPassengerSexuality() {
        return passengerSexuality;
    }

    public String getPassengerMail() {
        return passengerMail;
    }

    public String getPassengerMobile() {
        return passengerMobile;
    }

    public String getPassengerPassword() {
        return passengerPassword;
    }

    public String getResponse() {
        return response;
    }
}
