package com.app.wheelsonadminapp.model.driver;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DriverResponse{

	@SerializedName("path")
	private String path;

	@SerializedName("driver")
	private List<DriverItem> driver;

	@SerializedName("status")
	private int status;

	public void setPath(String path){
		this.path = path;
	}

	public String getPath(){
		return path;
	}

	public void setDriver(List<DriverItem> driver){
		this.driver = driver;
	}

	public List<DriverItem> getDriver(){
		return driver;
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
			"DriverResponse{" + 
			"path = '" + path + '\'' + 
			",driver = '" + driver + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}