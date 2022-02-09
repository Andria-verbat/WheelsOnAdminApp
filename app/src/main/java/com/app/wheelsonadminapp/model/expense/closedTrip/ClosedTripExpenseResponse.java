package com.app.wheelsonadminapp.model.expense.closedTrip;

import java.util.ArrayList;

/**
 * Created by Andria on 2/9/2022.
 */
public class ClosedTripExpenseResponse {
    public ArrayList<ClosedTripExpenseResponseItem> expense;
    public String path;
    public int status;


    public ArrayList<ClosedTripExpenseResponseItem> getExpense() {
        return expense;
    }

    public void setExpense(ArrayList<ClosedTripExpenseResponseItem> expense) {
        this.expense = expense;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
