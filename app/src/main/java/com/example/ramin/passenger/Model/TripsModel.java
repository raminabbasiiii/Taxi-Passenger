package com.example.ramin.passenger.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripsModel {

    @SerializedName("origin")
    @Expose
    private String origin;

    @SerializedName("trip_id")
    @Expose
    private int tripId;

    @SerializedName("sub_id")
    @Expose
    private int subId;

    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("date_of_movement")
    @Expose
    private String dateOfMovement;

    @SerializedName("time_of_movement")
    @Expose
    private String timeOfMovement;

    @SerializedName("path")
    @Expose
    private String path;

    @SerializedName("origin_min_lng")
    @Expose
    private double originMinLng;

    @SerializedName("origin_min_lat")
    @Expose
    private double originMinLat;

    @SerializedName("destination_max_lng")
    @Expose
    private double destinationMaxLng;

    @SerializedName("destination_max_lat")
    @Expose
    private double destinationMaxLat;

    public int getSubId() {
        return subId;
    }

    public double getOriginMinLng() {
        return originMinLng;
    }

    public double getOriginMinLat() {
        return originMinLat;
    }

    public double getDestinationMaxLng() {
        return destinationMaxLng;
    }

    public double getDestinationMaxLat() {
        return destinationMaxLat;
    }

    public String getPath() {
        return path;
    }

    public String getOrigin() {
        return origin;
    }

    public int getTripId() {
        return tripId;
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
