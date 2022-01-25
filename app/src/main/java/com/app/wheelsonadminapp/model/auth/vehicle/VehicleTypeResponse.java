package com.app.wheelsonadminapp.model.auth.vehicle;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class VehicleTypeResponse{

	@SerializedName("vehicletype")
	private List<VehicletypeItem> vehicletype;

	@SerializedName("status")
	private int status;

	public void setVehicletype(List<VehicletypeItem> vehicletype){
		this.vehicletype = vehicletype;
	}

	public List<VehicletypeItem> getVehicletype(){
		return vehicletype;
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
			"VehicleTypeResponse{" + 
			"vehicletype = '" + vehicletype + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}