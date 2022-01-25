package com.app.wheelsonadminapp.model.auth.vehicle;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class VehicletypeItem implements Parcelable {

	@SerializedName("vehicletype")
	private String vehicletype;

	@SerializedName("travelsid")
	private String travelsid;

	@SerializedName("id")
	private String id;

	public void setVehicletype(String vehicletype){
		this.vehicletype = vehicletype;
	}

	public String getVehicletype(){
		return vehicletype;
	}

	public void setTravelsid(String travelsid){
		this.travelsid = travelsid;
	}

	public String getTravelsid(){
		return travelsid;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"VehicletypeItem{" + 
			"vehicletype = '" + vehicletype + '\'' + 
			",travelsid = '" + travelsid + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.vehicletype);
		dest.writeString(this.travelsid);
		dest.writeString(this.id);
	}

	public void readFromParcel(Parcel source) {
		this.vehicletype = source.readString();
		this.travelsid = source.readString();
		this.id = source.readString();
	}

	public VehicletypeItem() {
	}

	protected VehicletypeItem(Parcel in) {
		this.vehicletype = in.readString();
		this.travelsid = in.readString();
		this.id = in.readString();
	}

	public static final Parcelable.Creator<VehicletypeItem> CREATOR = new Parcelable.Creator<VehicletypeItem>() {
		@Override
		public VehicletypeItem createFromParcel(Parcel source) {
			return new VehicletypeItem(source);
		}

		@Override
		public VehicletypeItem[] newArray(int size) {
			return new VehicletypeItem[size];
		}
	};
}