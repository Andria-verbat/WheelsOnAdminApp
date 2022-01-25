package com.app.wheelsonadminapp.model.service.vehicle_server_service;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class VehicleServiceListResponse{

	@SerializedName("path")
	private String path;

	@SerializedName("serviceDetails")
	private List<ServiceDetailsItem> serviceDetails;

	@SerializedName("status")
	private int status;

	public void setPath(String path){
		this.path = path;
	}

	public String getPath(){
		return path;
	}

	public void setServiceDetails(List<ServiceDetailsItem> serviceDetails){
		this.serviceDetails = serviceDetails;
	}

	public List<ServiceDetailsItem> getServiceDetails(){
		return serviceDetails;
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
			"VehicleServiceListResponse{" + 
			"path = '" + path + '\'' + 
			",serviceDetails = '" + serviceDetails + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}