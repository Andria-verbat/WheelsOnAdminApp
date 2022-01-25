package com.app.wheelsonadminapp.model.trip.triplist;

import com.google.gson.annotations.SerializedName;

public class TripListItem {

	@SerializedName("pickuptime")
	private String pickuptime;

	@SerializedName("amount")
	private String amount;

	@SerializedName("comments")
	private String comments;

	@SerializedName("drivername")
	private String drivername;

	@SerializedName("fromlat")
	private String fromlat;

	@SerializedName("tolon")
	private String tolon;

	@SerializedName("mobile1")
	private String mobile1;

	@SerializedName("kmrate")
	private String kmrate;

	@SerializedName("pickaddress")
	private String pickaddress;

	@SerializedName("startdate")
	private String startdate;

	@SerializedName("fromlon")
	private String fromlon;

	@SerializedName("tolat")
	private String tolat;

	@SerializedName("tolocation")
	private String tolocation;

	@SerializedName("enddate")
	private String enddate;

	@SerializedName("driverid")
	private String driverid;

	@SerializedName("dropaddress")
	private String dropaddress;

	@SerializedName("fromlocation")
	private String fromlocation;

	@SerializedName("travelsid")
	private String travelsid;

	@SerializedName("vehiclebrand")
	private String vehiclebrand;

	@SerializedName("person")
	private String person;

	@SerializedName("mobile2")
	private String mobile2;

	@SerializedName("vehiclemodel")
	private String vehiclemodel;

	@SerializedName("id")
	private String id;

	@SerializedName("vehicleid")
	private String vehicleid;

	public void setPickuptime(String pickuptime){
		this.pickuptime = pickuptime;
	}

	public String getPickuptime(){
		return pickuptime;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setComments(String comments){
		this.comments = comments;
	}

	public String getComments(){
		return comments;
	}

	public void setDrivername(String drivername){
		this.drivername = drivername;
	}

	public String getDrivername(){
		return drivername;
	}

	public void setFromlat(String fromlat){
		this.fromlat = fromlat;
	}

	public String getFromlat(){
		return fromlat;
	}

	public void setTolon(String tolon){
		this.tolon = tolon;
	}

	public String getTolon(){
		return tolon;
	}

	public void setMobile1(String mobile1){
		this.mobile1 = mobile1;
	}

	public String getMobile1(){
		return mobile1;
	}

	public void setKmrate(String kmrate){
		this.kmrate = kmrate;
	}

	public String getKmrate(){
		return kmrate;
	}

	public void setPickaddress(String pickaddress){
		this.pickaddress = pickaddress;
	}

	public String getPickaddress(){
		return pickaddress;
	}

	public void setStartdate(String startdate){
		this.startdate = startdate;
	}

	public String getStartdate(){
		return startdate;
	}

	public void setFromlon(String fromlon){
		this.fromlon = fromlon;
	}

	public String getFromlon(){
		return fromlon;
	}

	public void setTolat(String tolat){
		this.tolat = tolat;
	}

	public String getTolat(){
		return tolat;
	}

	public void setTolocation(String tolocation){
		this.tolocation = tolocation;
	}

	public String getTolocation(){
		return tolocation;
	}

	public void setEnddate(String enddate){
		this.enddate = enddate;
	}

	public String getEnddate(){
		return enddate;
	}

	public void setDriverid(String driverid){
		this.driverid = driverid;
	}

	public String getDriverid(){
		return driverid;
	}

	public void setDropaddress(String dropaddress){
		this.dropaddress = dropaddress;
	}

	public String getDropaddress(){
		return dropaddress;
	}

	public void setFromlocation(String fromlocation){
		this.fromlocation = fromlocation;
	}

	public String getFromlocation(){
		return fromlocation;
	}

	public void setTravelsid(String travelsid){
		this.travelsid = travelsid;
	}

	public String getTravelsid(){
		return travelsid;
	}

	public void setVehiclebrand(String vehiclebrand){
		this.vehiclebrand = vehiclebrand;
	}

	public String getVehiclebrand(){
		return vehiclebrand;
	}

	public void setPerson(String person){
		this.person = person;
	}

	public String getPerson(){
		return person;
	}

	public void setMobile2(String mobile2){
		this.mobile2 = mobile2;
	}

	public String getMobile2(){
		return mobile2;
	}

	public void setVehiclemodel(String vehiclemodel){
		this.vehiclemodel = vehiclemodel;
	}

	public String getVehiclemodel(){
		return vehiclemodel;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setVehicleid(String vehicleid){
		this.vehicleid = vehicleid;
	}

	public String getVehicleid(){
		return vehicleid;
	}

	@Override
 	public String toString(){
		return 
			"TripItem{" + 
			"pickuptime = '" + pickuptime + '\'' + 
			",amount = '" + amount + '\'' + 
			",comments = '" + comments + '\'' + 
			",drivername = '" + drivername + '\'' + 
			",fromlat = '" + fromlat + '\'' + 
			",tolon = '" + tolon + '\'' + 
			",mobile1 = '" + mobile1 + '\'' + 
			",kmrate = '" + kmrate + '\'' + 
			",pickaddress = '" + pickaddress + '\'' + 
			",startdate = '" + startdate + '\'' + 
			",fromlon = '" + fromlon + '\'' + 
			",tolat = '" + tolat + '\'' + 
			",tolocation = '" + tolocation + '\'' + 
			",enddate = '" + enddate + '\'' + 
			",driverid = '" + driverid + '\'' + 
			",dropaddress = '" + dropaddress + '\'' + 
			",fromlocation = '" + fromlocation + '\'' + 
			",travelsid = '" + travelsid + '\'' + 
			",vehiclebrand = '" + vehiclebrand + '\'' + 
			",person = '" + person + '\'' + 
			",mobile2 = '" + mobile2 + '\'' + 
			",vehiclemodel = '" + vehiclemodel + '\'' + 
			",id = '" + id + '\'' + 
			",vehicleid = '" + vehicleid + '\'' + 
			"}";
		}
}