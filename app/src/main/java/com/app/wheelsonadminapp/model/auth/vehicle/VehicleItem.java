package com.app.wheelsonadminapp.model.auth.vehicle;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class VehicleItem implements Parcelable {

	@SerializedName("vehicletype")
	private String vehicletype;

	@SerializedName("regno")
	private String regno;

	@SerializedName("rcbookimg")
	private String rcbookimg;

	@SerializedName("permitimg")
	private String permitimg;

	@SerializedName("taximg")
	private String taximg;

	@SerializedName("permitdate")
	private String permitdate;

	@SerializedName("insuranceimg")
	private String insuranceimg;

	@SerializedName("pollutionimg")
	private String pollutionimg;

	@SerializedName("pollutiondate")
	private String pollutiondate;

	@SerializedName("insurancedate")
	private String insurancedate;

	@SerializedName("seat")
	private String seat;

	@SerializedName("travelsid")
	private String travelsid;

	@SerializedName("model")
	private String model;

	@SerializedName("id")
	private String id;

	@SerializedName("brand")
	private String brand;

	@SerializedName("km")
	private String startKm;

	@SerializedName("vehiclename")
	private String vehicleNickName;

	public void setVehicletype(String vehicletype){
		this.vehicletype = vehicletype;
	}

	public String getVehicletype(){
		return vehicletype;
	}

	public void setRegno(String regno){
		this.regno = regno;
	}

	public String getRegno(){
		return regno;
	}

	public void setRcbookimg(String rcbookimg){
		this.rcbookimg = rcbookimg;
	}

	public String getRcbookimg(){
		return rcbookimg;
	}

	public void setPermitimg(String permitimg){
		this.permitimg = permitimg;
	}

	public String getPermitimg(){
		return permitimg;
	}

	public void setPermitdate(String permitdate){
		this.permitdate = permitdate;
	}

	public String getPermitdate(){
		return permitdate;
	}

	public void setInsuranceimg(String insuranceimg){
		this.insuranceimg = insuranceimg;
	}

	public String getInsuranceimg(){
		return insuranceimg;
	}

	public void setPollutionimg(String pollutionimg){
		this.pollutionimg = pollutionimg;
	}

	public String getPollutionimg(){
		return pollutionimg;
	}

	public void setPollutiondate(String pollutiondate){
		this.pollutiondate = pollutiondate;
	}

	public String getPollutiondate(){
		return pollutiondate;
	}

	public void setInsurancedate(String insurancedate){
		this.insurancedate = insurancedate;
	}

	public String getInsurancedate(){
		return insurancedate;
	}

	public void setSeat(String seat){
		this.seat = seat;
	}

	public String getSeat(){
		return seat;
	}

	public void setTravelsid(String travelsid){
		this.travelsid = travelsid;
	}

	public String getTravelsid(){
		return travelsid;
	}

	public void setModel(String model){
		this.model = model;
	}

	public String getModel(){
		return model;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setBrand(String brand){
		this.brand = brand;
	}

	public String getBrand(){
		return brand;
	}

	public String getTaximg() {
		return taximg;
	}

	public void setTaximg(String taximg) {
		this.taximg = taximg;
	}

	public String getStartKm() {
		return startKm;
	}

	public void setStartKm(String startKm) {
		this.startKm = startKm;
	}

	public String getVehicleNickName() {
		return vehicleNickName;
	}

	public void setVehicleNickName(String vehicleNickName) {
		this.vehicleNickName = vehicleNickName;
	}

	@Override
 	public String toString(){
		return 
			"VehicleItem{" + 
			"vehicletype = '" + vehicletype + '\'' + 
			",regno = '" + regno + '\'' + 
			",rcbookimg = '" + rcbookimg + '\'' + 
			",permitimg = '" + permitimg + '\'' + 
			",permitdate = '" + permitdate + '\'' + 
			",insuranceimg = '" + insuranceimg + '\'' + 
			",pollutionimg = '" + pollutionimg + '\'' + 
			",pollutiondate = '" + pollutiondate + '\'' + 
			",insurancedate = '" + insurancedate + '\'' + 
			",seat = '" + seat + '\'' + 
			",travelsid = '" + travelsid + '\'' + 
			",model = '" + model + '\'' + 
			",id = '" + id + '\'' + 
			",brand = '" + brand + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.vehicletype);
		dest.writeString(this.regno);
		dest.writeString(this.rcbookimg);
		dest.writeString(this.permitimg);
		dest.writeString(this.permitdate);
		dest.writeString(this.insuranceimg);
		dest.writeString(this.taximg);
		dest.writeString(this.pollutionimg);
		dest.writeString(this.pollutiondate);
		dest.writeString(this.insurancedate);
		dest.writeString(this.seat);
		dest.writeString(this.travelsid);
		dest.writeString(this.model);
		dest.writeString(this.id);
		dest.writeString(this.brand);
	}

	public void readFromParcel(Parcel source) {
		this.vehicletype = source.readString();
		this.regno = source.readString();
		this.rcbookimg = source.readString();
		this.permitimg = source.readString();
		this.permitdate = source.readString();
		this.insuranceimg = source.readString();
		this.pollutionimg = source.readString();
		this.taximg = source.readString();
		this.pollutiondate = source.readString();
		this.insurancedate = source.readString();
		this.seat = source.readString();
		this.travelsid = source.readString();
		this.model = source.readString();
		this.id = source.readString();
		this.brand = source.readString();
	}

	public VehicleItem() {
	}

	protected VehicleItem(Parcel in) {
		this.vehicletype = in.readString();
		this.regno = in.readString();
		this.rcbookimg = in.readString();
		this.permitimg = in.readString();
		this.permitdate = in.readString();
		this.insuranceimg = in.readString();
		this.taximg = in.readString();
		this.pollutionimg = in.readString();
		this.pollutiondate = in.readString();
		this.insurancedate = in.readString();
		this.seat = in.readString();
		this.travelsid = in.readString();
		this.model = in.readString();
		this.id = in.readString();
		this.brand = in.readString();
	}

	public static final Parcelable.Creator<VehicleItem> CREATOR = new Parcelable.Creator<VehicleItem>() {
		@Override
		public VehicleItem createFromParcel(Parcel source) {
			return new VehicleItem(source);
		}

		@Override
		public VehicleItem[] newArray(int size) {
			return new VehicleItem[size];
		}
	};
}