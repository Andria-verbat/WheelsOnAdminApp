package com.app.wheelsonadminapp.model.trip.triplist;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Andria on 1/26/2022.
 */
public class TripLiveListResponse {
    @SerializedName("trip")
    private List<TripLiveListItem> trip;

    @SerializedName("status")
    private int status;

    public List<TripLiveListItem> getTrip() {
        return trip;
    }

    public void setTrip(List<TripLiveListItem> trip) {
        this.trip = trip;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "TripLiveListResponse{" +
                        "trip = '" + trip + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
