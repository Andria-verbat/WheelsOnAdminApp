package com.app.wheelsonadminapp.model.states;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StatesResponse{

	@SerializedName("states")
	private List<StatesItem> states;

	public void setStates(List<StatesItem> states){
		this.states = states;
	}

	public List<StatesItem> getStates(){
		return states;
	}

	@Override
 	public String toString(){
		return 
			"StatesResponse{" + 
			"states = '" + states + '\'' + 
			"}";
		}
}