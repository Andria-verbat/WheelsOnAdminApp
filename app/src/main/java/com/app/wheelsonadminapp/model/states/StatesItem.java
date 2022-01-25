package com.app.wheelsonadminapp.model.states;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StatesItem{

	@SerializedName("capital")
	private String capital;

	@SerializedName("code")
	private String code;

	@SerializedName("name")
	private String name;

	@SerializedName("districts")
	private List<DistrictsItem> districts;

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

	public void setCapital(String capital){
		this.capital = capital;
	}

	public String getCapital(){
		return capital;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDistricts(List<DistrictsItem> districts){
		this.districts = districts;
	}

	public List<DistrictsItem> getDistricts(){
		return districts;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	@Override
 	public String toString(){
		return 
			"StatesItem{" + 
			"capital = '" + capital + '\'' + 
			",code = '" + code + '\'' + 
			",name = '" + name + '\'' + 
			",districts = '" + districts + '\'' + 
			",id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}
}