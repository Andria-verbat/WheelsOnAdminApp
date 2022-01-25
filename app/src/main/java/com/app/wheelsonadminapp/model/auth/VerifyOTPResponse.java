package com.app.wheelsonadminapp.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class VerifyOTPResponse implements Parcelable {

	@SerializedName("path")
	private String path;

	@SerializedName("travel")
	private List<TravelItem> travel;

	@SerializedName("status")
	private int status;

	public void setPath(String path){
		this.path = path;
	}

	public String getPath(){
		return path;
	}

	public void setTravel(List<TravelItem> travel){
		this.travel = travel;
	}

	public List<TravelItem> getTravel(){
		return travel;
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
			"VerifyOTPResponse{" + 
			"path = '" + path + '\'' + 
			",travel = '" + travel + '\'' + 
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
		dest.writeList(this.travel);
		dest.writeInt(this.status);
	}

	public void readFromParcel(Parcel source) {
		this.path = source.readString();
		this.travel = new ArrayList<TravelItem>();
		source.readList(this.travel, TravelItem.class.getClassLoader());
		this.status = source.readInt();
	}

	public VerifyOTPResponse() {
	}

	protected VerifyOTPResponse(Parcel in) {
		this.path = in.readString();
		this.travel = new ArrayList<TravelItem>();
		in.readList(this.travel, TravelItem.class.getClassLoader());
		this.status = in.readInt();
	}

	public static final Parcelable.Creator<VerifyOTPResponse> CREATOR = new Parcelable.Creator<VerifyOTPResponse>() {
		@Override
		public VerifyOTPResponse createFromParcel(Parcel source) {
			return new VerifyOTPResponse(source);
		}

		@Override
		public VerifyOTPResponse[] newArray(int size) {
			return new VerifyOTPResponse[size];
		}
	};
}