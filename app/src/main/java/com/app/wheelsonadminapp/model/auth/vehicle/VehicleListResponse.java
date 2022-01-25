package com.app.wheelsonadminapp.model.auth.vehicle;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class VehicleListResponse{

	@SerializedName("path")
	private Object path;

	@SerializedName("vehicle")
	private List<VehicleItem> vehicle;

	@SerializedName("status")
	private int status;

	public void setPath(Object path){
		this.path = path;
	}

	public Object getPath(){
		return path;
	}

	public void setVehicle(List<VehicleItem> vehicle){
		this.vehicle = vehicle;
	}

	public List<VehicleItem> getVehicle(){
		return vehicle;
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
			"VehicleListResponse{" + 
			"path = '" + path + '\'' + 
			",vehicle = '" + vehicle + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}