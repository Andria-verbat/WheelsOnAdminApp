package com.app.wheelsonadminapp.model.trip;

import com.app.wheelsonadminapp.model.driver.DriverItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Andria on 2/2/2022.
 */
public class PaymentStatusResponse {
    @SerializedName("paystatus")
    private List<PaymentStatusItem> paystatus;

    @SerializedName("status")
    private int status;


    public List<PaymentStatusItem> getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(List<PaymentStatusItem> paystatus) {
        this.paystatus = paystatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return
                "PaymentStatusResponse{" +
                        "paystatus = '" + paystatus + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}
