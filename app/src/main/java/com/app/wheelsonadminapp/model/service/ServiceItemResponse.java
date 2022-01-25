package com.app.wheelsonadminapp.model.service;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ServiceItemResponse{

	@SerializedName("service")
	private List<ServiceItem> service;

	@SerializedName("status")
	private int status;

	public void setService(List<ServiceItem> service){
		this.service = service;
	}

	public List<ServiceItem> getService(){
		return service;
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
			"ServiceItemResponse{" + 
			"service = '" + service + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}