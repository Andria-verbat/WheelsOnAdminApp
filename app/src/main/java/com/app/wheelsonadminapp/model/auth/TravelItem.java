package com.app.wheelsonadminapp.model.auth;

import com.google.gson.annotations.SerializedName;

public class TravelItem{

	@SerializedName("owner")
	private String owner;

	@SerializedName("no")
	private String no;

	@SerializedName("address")
	private String address;

	@SerializedName("phone")
	private String phone;

	@SerializedName("name")
	private String name;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("id")
	private Object id;

	@SerializedName("email")
	private String email;

	@SerializedName("token")
	private String token;

	@SerializedName("status")
	private Object status;

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

	public void setId(Object id){
		this.id = id;
	}

	public Object getId(){
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

	public void setStatus(Object status){
		this.status = status;
	}

	public Object getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"TravelItem{" + 
			"owner = '" + owner + '\'' + 
			",no = '" + no + '\'' + 
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