package com.app.wheelsonadminapp.model.trip.trip_details;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TripDetailsResponse implements Parcelable {

	@SerializedName("path")
	private String path;

	@SerializedName("trip")
	private List<TripDetailItem> trip;

	@SerializedName("expense")
	private List<ExpenseItem> expense;

	@SerializedName("status")
	private int status;

	public void setPath(String path){
		this.path = path;
	}

	public String getPath(){
		return path;
	}

	public void setTrip(List<TripDetailItem> trip){
		this.trip = trip;
	}

	public List<TripDetailItem> getTrip(){
		return trip;
	}

	public void setExpense(List<ExpenseItem> expense){
		this.expense = expense;
	}

	public List<ExpenseItem> getExpense(){
		return expense;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"TripDetailsResponse{" + 
			"path = '" + path + '\'' + 
			",trip = '" + trip + '\'' + 
			",expense = '" + expense + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.path);
		dest.writeTypedList(this.trip);
		dest.writeTypedList(this.expense);
		dest.writeInt(this.status);
	}

	public void readFromParcel(Parcel source) {
		this.path = source.readString();
		this.trip = source.createTypedArrayList(TripDetailItem.CREATOR);
		this.expense = source.createTypedArrayList(ExpenseItem.CREATOR);
		this.status = source.readInt();
	}

	public TripDetailsResponse() {
	}

	protected TripDetailsResponse(Parcel in) {
		this.path = in.readString();
		this.trip = in.createTypedArrayList(TripDetailItem.CREATOR);
		this.expense = in.createTypedArrayList(ExpenseItem.CREATOR);
		this.status = in.readInt();
	}

	public static final Parcelable.Creator<TripDetailsResponse> CREATOR = new Parcelable.Creator<TripDetailsResponse>() {
		@Override
		public TripDetailsResponse createFromParcel(Parcel source) {
			return new TripDetailsResponse(source);
		}

		@Override
		public TripDetailsResponse[] newArray(int size) {
			return new TripDetailsResponse[size];
		}
	};
}