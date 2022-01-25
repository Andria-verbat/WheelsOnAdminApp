package com.app.wheelsonadminapp.model.expense;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ExpenseItemListResponse{

	@SerializedName("expense")
	private List<ExpenseItem> expense;

	@SerializedName("status")
	private int status;

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
			"ExpenseItemListResponse{" + 
			"expense = '" + expense + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}