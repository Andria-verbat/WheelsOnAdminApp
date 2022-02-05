package com.app.wheelsonadminapp.model.trip.closed_trips;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andria on 2/4/2022.
 */
public class ClosedTripItems implements Parcelable {
    public String id;
    public String travelsid;
    public String fromlocation;
    public String fromlat;
    public String fromlon;
    public String tolocation;
    public String tolat;
    public String tolon;
    public String driverid;
    public String drivername;
    public String vehicleid;
    public String vehiclebrand;
    public String vehiclemodel;
    public String startdate;
    public String enddate;
    public String pickuptime;
    public String pickaddress;
    public String dropaddress;
    public String person;
    public String mobile1;
    public String mobile2;
    public String amount;
    public String kmrate;
    public String comments;
    public String startingkm;
    public String endkm;
    public String startimage;
    public String endimage;
    public String startremark;
    public String endremark;

    protected ClosedTripItems(Parcel in) {
        id = in.readString();
        travelsid = in.readString();
        fromlocation = in.readString();
        fromlat = in.readString();
        fromlon = in.readString();
        tolocation = in.readString();
        tolat = in.readString();
        tolon = in.readString();
        driverid = in.readString();
        drivername = in.readString();
        vehicleid = in.readString();
        vehiclebrand = in.readString();
        vehiclemodel = in.readString();
        startdate = in.readString();
        enddate = in.readString();
        pickuptime = in.readString();
        pickaddress = in.readString();
        dropaddress = in.readString();
        person = in.readString();
        mobile1 = in.readString();
        mobile2 = in.readString();
        amount = in.readString();
        kmrate = in.readString();
        comments = in.readString();
        startingkm = in.readString();
        endkm = in.readString();
        startimage = in.readString();
        endimage = in.readString();
        startremark = in.readString();
        endremark = in.readString();
    }

    public static final Creator<ClosedTripItems> CREATOR = new Creator<ClosedTripItems>() {
        @Override
        public ClosedTripItems createFromParcel(Parcel in) {
            return new ClosedTripItems(in);
        }

        @Override
        public ClosedTripItems[] newArray(int size) {
            return new ClosedTripItems[size];
        }
    };

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

    public String getStartingkm() {
        return startingkm;
    }

    public void setStartingkm(String startingkm) {
        this.startingkm = startingkm;
    }

    public String getEndkm() {
        return endkm;
    }

    public void setEndkm(String endkm) {
        this.endkm = endkm;
    }

    public String getStartimage() {
        return startimage;
    }

    public void setStartimage(String startimage) {
        this.startimage = startimage;
    }

    public String getEndimage() {
        return endimage;
    }

    public void setEndimage(String endimage) {
        this.endimage = endimage;
    }

    public String getStartremark() {
        return startremark;
    }

    public void setStartremark(String startremark) {
        this.startremark = startremark;
    }

    public String getEndremark() {
        return endremark;
    }

    public void setEndremark(String endremark) {
        this.endremark = endremark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
       dest.writeString(travelsid);
       dest.writeString(fromlocation);
       dest.writeString(fromlat);
       dest.writeString(fromlon);
       dest.writeString(tolocation);
       dest.writeString(tolat);
       dest.writeString(tolon);
       dest.writeString(driverid);
       dest.writeString(drivername);
       dest.writeString(vehicleid);
       dest.writeString(vehiclebrand);
       dest.writeString(vehiclemodel);
       dest.writeString(startdate);
       dest.writeString(enddate);
       dest.writeString(pickuptime);
       dest.writeString(pickaddress);
       dest.writeString(dropaddress);
       dest.writeString(person);
       dest.writeString(mobile1);
       dest.writeString(mobile2);
       dest.writeString(amount);
       dest.writeString(kmrate);
       dest.writeString(comments);
       dest.writeString(startingkm);
       dest.writeString(endkm);
       dest.writeString(startimage);
       dest.writeString(endimage);
    }
}