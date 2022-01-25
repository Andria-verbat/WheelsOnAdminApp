package com.app.wheelsonadminapp.model.service.vehicle_service;

import com.google.gson.annotations.SerializedName;

public class ServicedetailsItem{

	@SerializedName("vehicletype")
	private String vehicletype;

	@SerializedName("regno")
	private String regno;

	@SerializedName("travelsname")
	private String travelsname;

	@SerializedName("travelsowner")
	private String travelsowner;

	@SerializedName("travelsid")
	private String travelsid;

	@SerializedName("travelsmobile")
	private String travelsmobile;

	@SerializedName("model")
	private String model;

	@SerializedName("vehicleid")
	private String vehicleid;

	@SerializedName("brand")
	private String brand;

	public void setVehicletype(String vehicletype){
		this.vehicletype = vehicletype;
	}

	public String getVehicletype(){
		return vehicletype;
	}

	public void setRegno(String regno){
		this.regno = regno;
	}

	public String getRegno(){
		return regno;
	}

	public void setTravelsname(String travelsname){
		this.travelsname = travelsname;
	}

	public String getTravelsname(){
		return travelsname;
	}

	public void setTravelsowner(String travelsowner){
		this.travelsowner = travelsowner;
	}

	public String getTravelsowner(){
		return travelsowner;
	}

	public void setTravelsid(String travelsid){
		this.travelsid = travelsid;
	}

	public String getTravelsid(){
		return travelsid;
	}

	public void setTravelsmobile(String travelsmobile){
		this.travelsmobile = travelsmobile;
	}

	public String getTravelsmobile(){
		return travelsmobile;
	}

	public void setModel(String model){
		this.model = model;
	}

	public String getModel(){
		return model;
	}

	public void setVehicleid(String vehicleid){
		this.vehicleid = vehicleid;
	}

	public String getVehicleid(){
		return vehicleid;
	}

	public void setBrand(String brand){
		this.brand = brand;
	}

	public String getBrand(){
		return brand;
	}

	@Override
 	public String toString(){
		return 
			"ServicedetailsItem{" + 
			"vehicletype = '" + vehicletype + '\'' + 
			",regno = '" + regno + '\'' + 
			",travelsname = '" + travelsname + '\'' + 
			",travelsowner = '" + travelsowner + '\'' + 
			",travelsid = '" + travelsid + '\'' + 
			",travelsmobile = '" + travelsmobile + '\'' + 
			",model = '" + model + '\'' + 
			",vehicleid = '" + vehicleid + '\'' + 
			",brand = '" + brand + '\'' + 
			"}";
		}
}