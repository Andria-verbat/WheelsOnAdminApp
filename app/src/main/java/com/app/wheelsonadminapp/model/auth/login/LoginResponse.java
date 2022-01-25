package com.app.wheelsonadminapp.model.auth.login;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LoginResponse{

	@SerializedName("path")
	private String path;

	@SerializedName("travel")
	private List<TravelItem> travel;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setPath(String path){
		this.path = path;
	}

	public String getPath(){
		return path;
	}

	public void setTravel(List<TravelItem> travel){
		this.travel = travel;
	}

	public List<TravelItem> getTravel(){
		return travel;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
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
			"LoginResponse{" + 
			"path = '" + path + '\'' + 
			",travel = '" + travel + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}