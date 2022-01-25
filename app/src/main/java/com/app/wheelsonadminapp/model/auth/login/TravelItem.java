package com.app.wheelsonadminapp.model.auth.login;

import com.google.gson.annotations.SerializedName;

public class TravelItem{

	@SerializedName("image")
	private String image;

	@SerializedName("address")
	private String address;

	@SerializedName("travelname")
	private String travelname;

	@SerializedName("phone")
	private String phone;

	@SerializedName("ownername")
	private String ownername;

	@SerializedName("vehicles")
	private String vehicles;

	@SerializedName("emailid")
	private String emailid;

	@SerializedName("id")
	private String id;

	@SerializedName("mobileno")
	private String mobileno;

	@SerializedName("token")
	private String token;

	@SerializedName("status")
	private String status;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setTravelname(String travelname){
		this.travelname = travelname;
	}

	public String getTravelname(){
		return travelname;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setOwnername(String ownername){
		this.ownername = ownername;
	}

	public String getOwnername(){
		return ownername;
	}

	public void setVehicles(String vehicles){
		this.vehicles = vehicles;
	}

	public String getVehicles(){
		return vehicles;
	}

	public void setEmailid(String emailid){
		this.emailid = emailid;
	}

	public String getEmailid(){
		return emailid;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setMobileno(String mobileno){
		this.mobileno = mobileno;
	}

	public String getMobileno(){
		return mobileno;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"TravelItem{" + 
			"image = '" + image + '\'' + 
			",address = '" + address + '\'' + 
			",travelname = '" + travelname + '\'' + 
			",phone = '" + phone + '\'' + 
			",ownername = '" + ownername + '\'' + 
			",vehicles = '" + vehicles + '\'' + 
			",emailid = '" + emailid + '\'' + 
			",id = '" + id + '\'' + 
			",mobileno = '" + mobileno + '\'' + 
			",token = '" + token + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}