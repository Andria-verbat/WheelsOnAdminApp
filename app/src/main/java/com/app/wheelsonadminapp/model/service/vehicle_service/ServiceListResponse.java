package com.app.wheelsonadminapp.model.service.vehicle_service;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ServiceListResponse{

	@SerializedName("servicedetails")
	private List<ServicedetailsItem> servicedetails;

	@SerializedName("status")
	private int status;

	public void setServicedetails(List<ServicedetailsItem> servicedetails){
		this.servicedetails = servicedetails;
	}

	public List<ServicedetailsItem> getServicedetails(){
		return servicedetails;
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
			"ServiceListResponse{" + 
			"servicedetails = '" + servicedetails + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}