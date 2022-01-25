package com.app.wheelsonadminapp.model.trip;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TripResponse{

	@SerializedName("trip")
	private List<TripItem> trip;

	@SerializedName("status")
	private int status;

	public void setTrip(List<TripItem> trip){
		this.trip = trip;
	}

	public List<TripItem> getTrip(){
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
			"TripResponse{" + 
			"trip = '" + trip + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}