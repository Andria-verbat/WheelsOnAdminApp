package com.app.wheelsonadminapp.model.auth.register;

import com.google.gson.annotations.SerializedName;

public class TravelItem{

	@SerializedName("owner")
	private String owner;

	@SerializedName("no")
	private String no;

	@SerializedName("image")
	private String image;

	@SerializedName("address")
	private String address;

	@SerializedName("phone")
	private String phone;

	@SerializedName("name")
	private String name;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("id")
	private String id;

	@SerializedName("email")
	private String email;

	@SerializedName("token")
	private String token;

	@SerializedName("status")
	private String status;

	public void setOwner(String owner){
		this.owner = owner;
	}

	public String getOwner(){
		return owner;
	}

	public void setNo(String no){
		this.no = no;
	}

	public String getNo(){
		return no;
	}

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

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
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
			"owner = '" + owner + '\'' + 
			",no = '" + no + '\'' + 
			",image = '" + image + '\'' + 
			",address = '" + address + '\'' + 
			",phone = '" + phone + '\'' + 
			",name = '" + name + '\'' + 
			",mobile = '" + mobile + '\'' + 
			",id = '" + id + '\'' + 
			",email = '" + email + '\'' + 
			",token = '" + token + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}