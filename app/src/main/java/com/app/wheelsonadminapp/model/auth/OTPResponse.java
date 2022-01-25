package com.app.wheelsonadminapp.model.auth;

import com.google.gson.annotations.SerializedName;

public class OTPResponse{

	@SerializedName("data")
	private String data;

	@SerializedName("status")
	private int status;

	public void setData(String data){
		this.data = data;
	}

	public String getData(){
		return data;
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
			"OTPResponse{" + 
			"data = '" + data + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}