package com.example.ramin.passenger.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisteredTripsModel {

    @SerializedName("driver_name")
    @Expose
    private String driverName;

    @SerializedName("driver_family")
    @Expose
    private String driverFamily;

    @SerializedName("driver_car_model")
    @Expose
    private String driverCarModel;

    @SerializedName("driver_car_color")
    @Expose
    private String driverCarColor;

    @SerializedName("driver_image")
    @Expose
    private String driverImage;

    @SerializedName("driver_mobile")
    @Expose
    private String driverMobile;

    @SerializedName("cost_of_payment")
    @Expose
    private int costOfPayment;

    @SerializedName("trip_id")
    @Expose
    private int tripId;

    @SerializedName("sub_id")
    @Expose
    private int subId;

    @SerializedName("distance")
    @Expose
    private int distance;

    @SerializedName("origin")
    @Expose
    private String origin;

    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("date_of_movement")
    @Expose
    private String dateOfMovement;

    @SerializedName("time_of_movement")
    @Expose
    private String timeOfMovement;

    public int getSubId() {
        return subId;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverFamily() {
        return driverFamily;
    }

    public String getDriverCarModel() {
        return driverCarModel;
    }

    public String getDriverCarColor() {
        return driverCarColor;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public String getDriverMobile() {
        return driverMobile;
    }

    public int getCostOfPayment() {
        return costOfPayment;
    }

    public int getTripId() {
        return tripId;
    }

    public int getDistance() {
        return distance;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDateOfMovement() {
        return dateOfMovement;
    }

    public String getTimeOfMovement() {
        return timeOfMovement;
    }
}
