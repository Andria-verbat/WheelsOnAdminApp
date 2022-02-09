package com.app.wheelsonadminapp.model.expense.closedTrip;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andria on 2/9/2022.
 */
public class ClosedTripExpenseItem implements Parcelable {

    public String expenseid;
    public String expensetravelsid;
    public String expensetripid;
    public String expensetype;
    public String expense_amount;
    public String expenseremark;
    public String expenseimage;

    public String getExpenseid() {
        return expenseid;
    }

    public void setExpenseid(String expenseid) {
        this.expenseid = expenseid;
    }

    public String getExpensetravelsid() {
        return expensetravelsid;
    }

    public void setExpensetravelsid(String expensetravelsid) {
        this.expensetravelsid = expensetravelsid;
    }

    public String getExpensetripid() {
        return expensetripid;
    }

    public void setExpensetripid(String expensetripid) {
        this.expensetripid = expensetripid;
    }

    public String getExpensetype() {
        return expensetype;
    }

    public void setExpensetype(String expensetype) {
        this.expensetype = expensetype;
    }

    public String getExpense_amount() {
        return expense_amount;
    }

    public void setExpense_amount(String expense_amount) {
        this.expense_amount = expense_amount;
    }

    public String getExpenseremark() {
        return expenseremark;
    }

    public void setExpenseremark(String expenseremark) {
        this.expenseremark = expenseremark;
    }

    public String getExpenseimage() {
        return expenseimage;
    }

    public void setExpenseimage(String expenseimage) {
        this.expenseimage = expenseimage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
