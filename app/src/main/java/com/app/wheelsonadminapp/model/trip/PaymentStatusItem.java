package com.app.wheelsonadminapp.model.trip;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Andria on 2/2/2022.
 */
public class PaymentStatusItem {

    @SerializedName("payid")
    private String payid;
    @SerializedName("payname")
    private String payname;

    public String getPayid() {
        return payid;
    }

    public void setPayid(String payid) {
        this.payid = payid;
    }

    public String getPayname() {
        return payname;
    }

    public void setPayname(String payname) {
        this.payname = payname;
    }

    @Override
    public String toString(){
        return
                "PaymentStatusItem{" +
                        "payid = '" + payid + '\'' +
                        ",payname = '" + payname + '\'' +
                        "}";
    }
}
