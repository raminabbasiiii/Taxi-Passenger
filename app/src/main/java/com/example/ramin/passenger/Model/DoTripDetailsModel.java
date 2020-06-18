package com.example.ramin.passenger.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoTripDetailsModel {

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

    @SerializedName("cost_of_payment")
    @Expose
    private int costOfPayment;

    @SerializedName("payment_type")
    @Expose
    private String paymentType;

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

    public int getCostOfPayment() {
        return costOfPayment;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setDriverFamily(String driverFamily) {
        this.driverFamily = driverFamily;
    }

    public void setDriverCarModel(String driverCarModel) {
        this.driverCarModel = driverCarModel;
    }

    public void setDriverCarColor(String driverCarColor) {
        this.driverCarColor = driverCarColor;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDateOfMovement(String dateOfMovement) {
        this.dateOfMovement = dateOfMovement;
    }

    public void setTimeOfMovement(String timeOfMovement) {
        this.timeOfMovement = timeOfMovement;
    }

    public void setCostOfPayment(int costOfPayment) {
        this.costOfPayment = costOfPayment;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
