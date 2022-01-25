package com.app.wheelsonadminapp.model.trip.trip_track;

import com.google.gson.annotations.SerializedName;

public class TrackTripItem {

	@SerializedName("vehicleregno")
	private String vehicleregno;

	@SerializedName("travelsowner")
	private String travelsowner;

	@SerializedName("drivername")
	private String drivername;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("tripid")
	private String tripid;

	@SerializedName("travelsname")
	private String travelsname;

	@SerializedName("driverid")
	private String driverid;

	@SerializedName("travelsid")
	private String travelsid;

	@SerializedName("location")
	private String location;

	@SerializedName("from")
	private String from;

	@SerializedName("id")
	private String id;

	@SerializedName("to")
	private String to;

	@SerializedName("vehicleid")
	private String vehicleid;

	@SerializedName("longitude")
	private String longitude;

	public void setVehicleregno(String vehicleregno){
		this.vehicleregno = vehicleregno;
	}

	public String getVehicleregno(){
		return vehicleregno;
	}

	public void setTravelsowner(String travelsowner){
		this.travelsowner = travelsowner;
	}

	public String getTravelsowner(){
		return travelsowner;
	}

	public void setDrivername(String drivername){
		this.drivername = drivername;
	}

	public String getDrivername(){
		return drivername;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setTripid(String tripid){
		this.tripid = tripid;
	}

	public String getTripid(){
		return tripid;
	}

	public void setTravelsname(String travelsname){
		this.travelsname = travelsname;
	}

	public String getTravelsname(){
		return travelsname;
	}

	public void setDriverid(String driverid){
		this.driverid = driverid;
	}

	public String getDriverid(){
		return driverid;
	}

	public void setTravelsid(String travelsid){
		this.travelsid = travelsid;
	}

	public String getTravelsid(){
		return travelsid;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLocation(){
		return location;
	}

	public void setFrom(String from){
		this.from = from;
	}

	public String getFrom(){
		return from;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTo(String to){
		this.to = to;
	}

	public String getTo(){
		return to;
	}

	public void setVehicleid(String vehicleid){
		this.vehicleid = vehicleid;
	}

	public String getVehicleid(){
		return vehicleid;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	@Override
 	public String toString(){
		return 
			"TripItem{" + 
			"vehicleregno = '" + vehicleregno + '\'' + 
			",travelsowner = '" + travelsowner + '\'' + 
			",drivername = '" + drivername + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",tripid = '" + tripid + '\'' + 
			",travelsname = '" + travelsname + '\'' + 
			",driverid = '" + driverid + '\'' + 
			",travelsid = '" + travelsid + '\'' + 
			",location = '" + location + '\'' + 
			",from = '" + from + '\'' + 
			",id = '" + id + '\'' + 
			",to = '" + to + '\'' + 
			",vehicleid = '" + vehicleid + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}
}