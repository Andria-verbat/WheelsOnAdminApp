package com.app.wheelsonadminapp.model.trip.triplist;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Andria on 1/26/2022.
 */
public class TripLiveListItem {
    @SerializedName("id")
    public String id;
    @SerializedName("travelsid")
    public String travelsid;
    @SerializedName("fromlocation")
    public String fromlocation;
    @SerializedName("fromlat")
    public String fromlat;
    @SerializedName("fromlon")
    public String fromlon;
    @SerializedName("tolocation")
    public String tolocation;
    @SerializedName("tolat")
    public String tolat;
    @SerializedName("tolon")
    public String tolon;
    @SerializedName("driverid")
    public String driverid;
    @SerializedName("drivername")
    public String drivername;
    @SerializedName("vehicleid")
    public String vehicleid;
    @SerializedName("vehiclebrand")
    public String vehiclebrand;
    @SerializedName("vehiclemodel")
    public String vehiclemodel;
    @SerializedName("startdate")
    public String startdate;
    @SerializedName("enddate")
    public String enddate;
    @SerializedName("pickuptime")
    public String pickuptime;
    @SerializedName("pickaddress")
    public String pickaddress;
    @SerializedName("dropaddress")
    public String dropaddress;
    @SerializedName("person")
    public String person;
    @SerializedName("mobile1")
    public String mobile1;
    @SerializedName("mobile2")
    public String mobile2;
    @SerializedName("amount")
    public String amount;
    @SerializedName("kmrate")
    public String kmrate;
    @SerializedName("comments")
    public String comments;
    @SerializedName("tripstartid")
    public String tripstartid;
    @SerializedName("tripstartkm")
    public String tripstartkm;
    @SerializedName("tripstartimage")
    public String tripstartimage;
    @SerializedName("tpstatus_stremark")
    public String tpstatus_stremark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTravelsid() {
        return travelsid;
    }

    public void setTravelsid(String travelsid) {
        this.travelsid = travelsid;
    }

    public String getFromlocation() {
        return fromlocation;
    }

    public void setFromlocation(String fromlocation) {
        this.fromlocation = fromlocation;
    }

    public String getFromlat() {
        return fromlat;
    }

    public void setFromlat(String fromlat) {
        this.fromlat = fromlat;
    }

    public String getFromlon() {
        return fromlon;
    }

    public void setFromlon(String fromlon) {
        this.fromlon = fromlon;
    }

    public String getTolocation() {
        return tolocation;
    }

    public void setTolocation(String tolocation) {
        this.tolocation = tolocation;
    }

    public String getTolat() {
        return tolat;
    }

    public void setTolat(String tolat) {
        this.tolat = tolat;
    }

    public String getTolon() {
        return tolon;
    }

    public void setTolon(String tolon) {
        this.tolon = tolon;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getVehicleid() {
        return vehicleid;
    }

    public void setVehicleid(String vehicleid) {
        this.vehicleid = vehicleid;
    }

    public String getVehiclebrand() {
        return vehiclebrand;
    }

    public void setVehiclebrand(String vehiclebrand) {
        this.vehiclebrand = vehiclebrand;
    }

    public String getVehiclemodel() {
        return vehiclemodel;
    }

    public void setVehiclemodel(String vehiclemodel) {
        this.vehiclemodel = vehiclemodel;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getPickuptime() {
        return pickuptime;
    }

    public void setPickuptime(String pickuptime) {
        this.pickuptime = pickuptime;
    }

    public String getPickaddress() {
        return pickaddress;
    }

    public void setPickaddress(String pickaddress) {
        this.pickaddress = pickaddress;
    }

    public String getDropaddress() {
        return dropaddress;
    }

    public void setDropaddress(String dropaddress) {
        this.dropaddress = dropaddress;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getKmrate() {
        return kmrate;
    }

    public void setKmrate(String kmrate) {
        this.kmrate = kmrate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTripstartid() {
        return tripstartid;
    }

    public void setTripstartid(String tripstartid) {
        this.tripstartid = tripstartid;
    }

    public String getTripstartkm() {
        return tripstartkm;
    }

    public void setTripstartkm(String tripstartkm) {
        this.tripstartkm = tripstartkm;
    }

    public String getTripstartimage() {
        return tripstartimage;
    }

    public void setTripstartimage(String tripstartimage) {
        this.tripstartimage = tripstartimage;
    }

    public String getTpstatus_stremark() {
        return tpstatus_stremark;
    }

    public void setTpstatus_stremark(String tpstatus_stremark) {
        this.tpstatus_stremark = tpstatus_stremark;
    }


    @Override
    public String toString(){
        return
                "TripLiveListItem{" +
                        "id = '" + id + '\'' +
                        ",travelsid = '" + travelsid + '\'' +
                        ",fromlocation = '" + fromlocation + '\'' +
                        ",fromlat = '" + fromlat + '\'' +
                        ",fromlon = '" + fromlon + '\'' +
                        ",tolocation = '" + tolocation + '\'' +
                        ",tolat = '" + tolat + '\'' +
                        ",tolon = '" + tolon + '\'' +
                        ",driverid = '" + driverid + '\'' +
                        ",drivername = '" + drivername + '\'' +
                        ",vehicleid = '" + vehicleid + '\'' +
                        ",vehiclebrand = '" + vehiclebrand + '\'' +
                        ",vehiclemodel = '" + vehiclemodel + '\'' +
                        ",startdate = '" + startdate + '\'' +
                        ",enddate = '" + enddate + '\'' +
                        ",pickuptime = '" + pickuptime + '\'' +
                        ",pickaddress = '" + pickaddress + '\'' +
                        ",dropaddress = '" + dropaddress + '\'' +
                        ",person = '" + person + '\'' +
                        ",mobile1 = '" + mobile1 + '\'' +
                        ",mobile2 = '" + mobile2 + '\'' +
                        ",amount = '" + amount + '\'' +
                        ",kmrate = '" + kmrate + '\'' +
                        ",comments = '" + comments + '\'' +
                        ",tripstartid = '" + tripstartid + '\'' +
                        ",tripstartkm = '" + tripstartkm + '\'' +
                        ",tripstartimage = '" + tripstartimage + '\'' +
                        ",tpstatus_stremark = '" + tpstatus_stremark + '\'' +
                        "}";
    }
}
