package com.example.ramin.passenger.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReserveTripModel {

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

    @SerializedName("cooler")
    @Expose
    private String cooler;

    @SerializedName("heater")
    @Expose
    private String heater;

    @SerializedName("price")
    @Expose
    private int price;

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

    @SerializedName("stopping")
    @Expose
    private String stopping;

    @SerializedName("empty_chair_count")
    @Expose
    private int emptyChairCount;

    @SerializedName("shipment_capacity")
    @Expose
    private String shipmentCapacity;

    public int getSubId() {
        return subId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverFamily() {
        return driverFamily;
    }

    public void setDriverFamily(String driverFamily) {
        this.driverFamily = driverFamily;
    }

    public String getDriverCarModel() {
        return driverCarModel;
    }

    public void setDriverCarModel(String driverCarModel) {
        this.driverCarModel = driverCarModel;
    }

    public String getDriverCarColor() {
        return driverCarColor;
    }

    public void setDriverCarColor(String driverCarColor) {
        this.driverCarColor = driverCarColor;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getCooler() {
        return cooler;
    }

    public void setCooler(String cooler) {
        this.cooler = cooler;
    }

    public String getHeater() {
        return heater;
    }

    public void setHeater(String heater) {
        this.heater = heater;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateOfMovement() {
        return dateOfMovement;
    }

    public void setDateOfMovement(String dateOfMovement) {
        this.dateOfMovement = dateOfMovement;
    }

    public String getTimeOfMovement() {
        return timeOfMovement;
    }

    public void setTimeOfMovement(String timeOfMovement) {
        this.timeOfMovement = timeOfMovement;
    }

    public String getStopping() {
        return stopping;
    }

    public void setStopping(String stopping) {
        this.stopping = stopping;
    }

    public int getEmptyChairCount() {
        return emptyChairCount;
    }

    public void setEmptyChairCount(int emptyChairCount) {
        this.emptyChairCount = emptyChairCount;
    }

    public String getShipmentCapacity() {
        return shipmentCapacity;
    }

    public void setShipmentCapacity(String shipmentCapacity) {
        this.shipmentCapacity = shipmentCapacity;
    }

}
