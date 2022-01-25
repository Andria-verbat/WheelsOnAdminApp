package com.app.wheelsonadminapp.model.expense.expense_list;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ExpenseListResponse{

	@SerializedName("path")
	private String path;

	@SerializedName("expense")
	private List<ExpenseItem> expense;

	@SerializedName("status")
	private int status;

	public void setPath(String path){
		this.path = path;
	}

	public String getPath(){
		return path;
	}

	public void setExpense(List<ExpenseItem> expense){
		this.expense = expense;
	}

	public List<ExpenseItem> getExpense(){
		return expense;
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
			"ExpenseListResponse{" + 
			"path = '" + path + '\'' + 
			",expense = '" + expense + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}