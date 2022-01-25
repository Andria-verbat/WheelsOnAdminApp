package com.app.wheelsonadminapp.model.service.vehicle_server_service;

import com.google.gson.annotations.SerializedName;

public class ServiceDetailsItem{

	@SerializedName("servicetype")
	private String servicetype;

	@SerializedName("servicedetails")
	private String servicedetails;

	@SerializedName("servicedate")
	private String servicedate;

	@SerializedName("servicevehicleid")
	private String servicevehicleid;

	@SerializedName("servicekm")
	private String servicekm;

	@SerializedName("serviceid")
	private String serviceid;

	@SerializedName("servicetypeid")
	private String servicetypeid;

	public void setServicetype(String servicetype){
		this.servicetype = servicetype;
	}

	public String getServicetype(){
		return servicetype;
	}

	public void setServicedetails(String servicedetails){
		this.servicedetails = servicedetails;
	}

	public String getServicedetails(){
		return servicedetails;
	}

	public void setServicedate(String servicedate){
		this.servicedate = servicedate;
	}

	public String getServicedate(){
		return servicedate;
	}

	public void setServicevehicleid(String servicevehicleid){
		this.servicevehicleid = servicevehicleid;
	}

	public String getServicevehicleid(){
		return servicevehicleid;
	}

	public void setServicekm(String servicekm){
		this.servicekm = servicekm;
	}

	public String getServicekm(){
		return servicekm;
	}

	public void setServiceid(String serviceid){
		this.serviceid = serviceid;
	}

	public String getServiceid(){
		return serviceid;
	}

	public String getServicetypeid() {
		return servicetypeid;
	}

	public void setServicetypeid(String servicetypeid) {
		this.servicetypeid = servicetypeid;
	}

	@Override
 	public String toString(){
		return 
			"ServiceDetailsItem{" + 
			"servicetype = '" + servicetype + '\'' + 
			",servicedetails = '" + servicedetails + '\'' + 
			",servicedate = '" + servicedate + '\'' + 
			",servicevehicleid = '" + servicevehicleid + '\'' + 
			",servicekm = '" + servicekm + '\'' + 
			",serviceid = '" + serviceid + '\'' + 
			"}";
		}
}