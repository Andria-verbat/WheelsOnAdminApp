package com.app.wheelsonadminapp.model.expense.expense_list;

import com.google.gson.annotations.SerializedName;

public class ExpenseItem{

	@SerializedName("expensedate")
	private String expensedate;

	@SerializedName("expense_amount")
	private String expenseAmount;

	@SerializedName("expensetripid")
	private String expensetripid;

	@SerializedName("expenseid")
	private String expenseid;

	@SerializedName("expenseimage")
	private String expenseimage;

	@SerializedName("expensetypeid")
	private String expensetypeid;

	@SerializedName("expenseremark")
	private String expenseremark;

	@SerializedName("expensetype")
	private String expensetype;

	public void setExpensedate(String expensedate){
		this.expensedate = expensedate;
	}

	public String getExpensedate(){
		return expensedate;
	}

	public void setExpenseAmount(String expenseAmount){
		this.expenseAmount = expenseAmount;
	}

	public String getExpenseAmount(){
		return expenseAmount;
	}

	public void setExpensetripid(String expensetripid){
		this.expensetripid = expensetripid;
	}

	public String getExpensetripid(){
		return expensetripid;
	}

	public void setExpenseid(String expenseid){
		this.expenseid = expenseid;
	}

	public String getExpenseid(){
		return expenseid;
	}

	public void setExpenseimage(String expenseimage){
		this.expenseimage = expenseimage;
	}

	public String getExpenseimage(){
		return expenseimage;
	}

	public void setExpensetypeid(String expensetypeid){
		this.expensetypeid = expensetypeid;
	}

	public String getExpensetypeid(){
		return expensetypeid;
	}

	public String getExpenseremark() {
		return expenseremark;
	}

	public void setExpenseremark(String expenseremark) {
		this.expenseremark = expenseremark;
	}

	public String getExpensetype() {
		return expensetype;
	}

	public void setExpensetype(String expensetype) {
		this.expensetype = expensetype;
	}

	@Override
 	public String toString(){
		return 
			"ExpenseItem{" + 
			"expensedate = '" + expensedate + '\'' + 
			",expense_amount = '" + expenseAmount + '\'' + 
			",expensetripid = '" + expensetripid + '\'' + 
			",expenseid = '" + expenseid + '\'' + 
			",expenseimage = '" + expenseimage + '\'' + 
			",expensetypeid = '" + expensetypeid + '\'' + 
			"}";
		}
}