package com.app.wheelsonadminapp.model.expense;

import com.google.gson.annotations.SerializedName;

public class ExpenseItem{

	@SerializedName("expensetypename")
	private String expensetypename;

	@SerializedName("expensetypeid")
	private String expensetypeid;

	public void setExpensetypename(String expensetypename){
		this.expensetypename = expensetypename;
	}

	public String getExpensetypename(){
		return expensetypename;
	}

	public void setExpensetypeid(String expensetypeid){
		this.expensetypeid = expensetypeid;
	}

	public String getExpensetypeid(){
		return expensetypeid;
	}

	@Override
 	public String toString(){
		return 
			"ExpenseItem{" + 
			"expensetypename = '" + expensetypename + '\'' + 
			",expensetypeid = '" + expensetypeid + '\'' + 
			"}";
		}
}