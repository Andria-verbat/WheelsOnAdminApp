package com.app.wheelsonadminapp.model.trip.triplist;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TripListResponse{

	@SerializedName("trip")
	private List<TripListItem> trip;

	@SerializedName("status")
	private int status;

	public void setTrip(List<TripListItem> trip){
		this.trip = trip;
	}

	public List<TripListItem> getTrip(){
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
			"TripListResponse{" + 
			"trip = '" + trip + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}