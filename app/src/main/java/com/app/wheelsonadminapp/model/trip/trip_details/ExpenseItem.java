package com.app.wheelsonadminapp.model.trip.trip_details;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ExpenseItem implements Parcelable {

	@SerializedName("other")
	private String other;

	@SerializedName("fuel")
	private String fuel;

	@SerializedName("toll")
	private String toll;

	@SerializedName("park")
	private String park;

	public void setOther(String other){
		this.other = other;
	}

	public String getOther(){
		return other;
	}

	public void setFuel(String fuel){
		this.fuel = fuel;
	}

	public String getFuel(){
		return fuel;
	}

	public void setToll(String toll){
		this.toll = toll;
	}

	public String getToll(){
		return toll;
	}

	public void setPark(String park){
		this.park = park;
	}

	public String getPark(){
		return park;
	}

	@Override
 	public String toString(){
		return 
			"ExpenseItem{" + 
			"other = '" + other + '\'' + 
			",fuel = '" + fuel + '\'' + 
			",toll = '" + toll + '\'' + 
			",park = '" + park + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.other);
		dest.writeString(this.fuel);
		dest.writeString(this.toll);
		dest.writeString(this.park);
	}

	public void readFromParcel(Parcel source) {
		this.other = source.readString();
		this.fuel = source.readString();
		this.toll = source.readString();
		this.park = source.readString();
	}

	public ExpenseItem() {
	}

	protected ExpenseItem(Parcel in) {
		this.other = in.readString();
		this.fuel = in.readString();
		this.toll = in.readString();
		this.park = in.readString();
	}

	public static final Parcelable.Creator<ExpenseItem> CREATOR = new Parcelable.Creator<ExpenseItem>() {
		@Override
		public ExpenseItem createFromParcel(Parcel source) {
			return new ExpenseItem(source);
		}

		@Override
		public ExpenseItem[] newArray(int size) {
			return new ExpenseItem[size];
		}
	};
}