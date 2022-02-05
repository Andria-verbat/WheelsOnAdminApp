package com.app.wheelsonadminapp.model.trip.closed_trips;

import java.util.ArrayList;

/**
 * Created by Andria on 2/4/2022.
 */
public class ClosedTripResponse {
    public ArrayList<ClosedTripItems> trip;
    public String path;
    public int status;

    public ArrayList<ClosedTripItems> getTrip() {
        return trip;
    }

    public void setTrip(ArrayList<ClosedTripItems> trip) {
        this.trip = trip;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
