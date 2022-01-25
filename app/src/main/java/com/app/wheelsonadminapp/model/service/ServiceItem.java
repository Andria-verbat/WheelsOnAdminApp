package com.app.wheelsonadminapp.model.service;

import com.google.gson.annotations.SerializedName;

public class ServiceItem{

	@SerializedName("servictypename")
	private String servicename;

	@SerializedName("servicetypeid")
	private String serviceid;

	public void setServicename(String servicename){
		this.servicename = servicename;
	}

	public String getServicename(){
		return servicename;
	}

	public void setServiceid(String serviceid){
		this.serviceid = serviceid;
	}

	public String getServiceid(){
		return serviceid;
	}

	@Override
 	public String toString(){
		return 
			"ServiceItem{" + 
			"servicename = '" + servicename + '\'' + 
			",serviceid = '" + serviceid + '\'' + 
			"}";
		}
}