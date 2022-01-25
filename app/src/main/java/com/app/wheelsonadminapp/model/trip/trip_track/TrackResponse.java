package com.app.wheelsonadminapp.model.trip.trip_track;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TrackResponse{

	@SerializedName("trip")
	private List<TrackTripItem> trip;

	@SerializedName("status")
	private int status;

	public void setTrip(List<TrackTripItem> trip){
		this.trip = trip;
	}

	public List<TrackTripItem> getTrip(){
		return trip;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"TrackResponse{" + 
			"trip = '" + trip + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}