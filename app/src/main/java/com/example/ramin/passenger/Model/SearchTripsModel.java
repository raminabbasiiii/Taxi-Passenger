package com.example.ramin.passenger.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchTripsModel {

    @SerializedName("origin")
    @Expose
    private String origin;

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("date_of_movement")
    @Expose
    private String dateOfMovement;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("trip_id")
    @Expose
    private int tripId;

    @SerializedName("time_of_movement")
    @Expose
    private String timeOfMovement;

    @SerializedName("empty_chair_count")
    @Expose
    private int emptyChairCount;

    @SerializedName("sub_id")
    @Expose
    private int subId;

    public int getSubId() {
        return subId;
    }

    public String getResponse() {
        return response;
    }

    public int getEmptyChairCount() {
        return emptyChairCount;
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

    public int getPrice() {
        return price;
    }

    public int getTripId() {
        return tripId;
    }

    public String getTimeOfMovement() {
        return timeOfMovement;
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

    public void setPrice(int price) {
        this.price = price;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public void setTimeOfMovement(String timeOfMovement) {
        this.timeOfMovement = timeOfMovement;
    }

    public void setEmptyChairCount(int emptyChairCount) {
        this.emptyChairCount = emptyChairCount;
    }
}
