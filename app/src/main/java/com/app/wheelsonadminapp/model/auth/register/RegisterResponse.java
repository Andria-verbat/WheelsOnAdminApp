package com.app.wheelsonadminapp.model.auth.register;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RegisterResponse{

	@SerializedName("path")
	private String path;

	@SerializedName("travel")
	private List<TravelItem> travel;

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

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"RegisterResponse{" + 
			"path = '" + path + '\'' + 
			",travel = '" + travel + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}