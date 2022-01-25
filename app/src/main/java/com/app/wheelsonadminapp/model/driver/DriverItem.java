package com.app.wheelsonadminapp.model.driver;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DriverItem implements Parcelable {

	@SerializedName("licenseno")
	private String licenseno;

	@SerializedName("licensefrontimg")
	private String licensefrontimg;

	@SerializedName("address")
	private String address;

	@SerializedName("aadharbackimg")
	private String aadharbackimg;

	@SerializedName("city")
	private String city;

	@SerializedName("licensevalidity")
	private String licensevalidity;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("driverimg")
	private String driverimg;

	@SerializedName("aadharno")
	private String aadharno;

	@SerializedName("experience")
	private String experience;

	@SerializedName("token")
	private String token;

	@SerializedName("licensecategory")
	private String licensecategory;

	@SerializedName("travelsid")
	private String travelsid;

	@SerializedName("licensebackimg")
	private String licensebackimg;

	@SerializedName("district")
	private String district;

	@SerializedName("name")
	private String name;

	@SerializedName("aadharfrontimg")
	private String aadharfrontimg;

	@SerializedName("id")
	private String id;

	@SerializedName("state")
	private String state;

	@SerializedName("policestation")
	private String policestation;

	@SerializedName("email")
	private String email;

	@SerializedName("status")
	private String status;

	public void setLicenseno(String licenseno){
		this.licenseno = licenseno;
	}

	public String getLicenseno(){
		return licenseno;
	}

	public void setLicensefrontimg(String licensefrontimg){
		this.licensefrontimg = licensefrontimg;
	}

	public String getLicensefrontimg(){
		return licensefrontimg;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setAadharbackimg(String aadharbackimg){
		this.aadharbackimg = aadharbackimg;
	}

	public String getAadharbackimg(){
		return aadharbackimg;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setLicensevalidity(String licensevalidity){
		this.licensevalidity = licensevalidity;
	}

	public String getLicensevalidity(){
		return licensevalidity;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setDriverimg(String driverimg){
		this.driverimg = driverimg;
	}

	public String getDriverimg(){
		return driverimg;
	}

	public void setAadharno(String aadharno){
		this.aadharno = aadharno;
	}

	public String getAadharno(){
		return aadharno;
	}

	public void setExperience(String experience){
		this.experience = experience;
	}

	public String getExperience(){
		return experience;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	public void setLicensecategory(String licensecategory){
		this.licensecategory = licensecategory;
	}

	public String getLicensecategory(){
		return licensecategory;
	}

	public void setTravelsid(String travelsid){
		this.travelsid = travelsid;
	}

	public String getTravelsid(){
		return travelsid;
	}

	public void setLicensebackimg(String licensebackimg){
		this.licensebackimg = licensebackimg;
	}

	public String getLicensebackimg(){
		return licensebackimg;
	}

	public void setDistrict(String district){
		this.district = district;
	}

	public String getDistrict(){
		return district;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setAadharfrontimg(String aadharfrontimg){
		this.aadharfrontimg = aadharfrontimg;
	}

	public String getAadharfrontimg(){
		return aadharfrontimg;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setPolicestation(String policestation){
		this.policestation = policestation;
	}

	public String getPolicestation(){
		return policestation;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"DriverItem{" + 
			"licenseno = '" + licenseno + '\'' + 
			",licensefrontimg = '" + licensefrontimg + '\'' + 
			",address = '" + address + '\'' + 
			",aadharbackimg = '" + aadharbackimg + '\'' + 
			",city = '" + city + '\'' + 
			",licensevalidity = '" + licensevalidity + '\'' + 
			",mobile = '" + mobile + '\'' + 
			",driverimg = '" + driverimg + '\'' + 
			",aadharno = '" + aadharno + '\'' + 
			",experience = '" + experience + '\'' + 
			",token = '" + token + '\'' + 
			",licensecategory = '" + licensecategory + '\'' + 
			",travelsid = '" + travelsid + '\'' + 
			",licensebackimg = '" + licensebackimg + '\'' + 
			",district = '" + district + '\'' + 
			",name = '" + name + '\'' + 
			",aadharfrontimg = '" + aadharfrontimg + '\'' + 
			",id = '" + id + '\'' + 
			",state = '" + state + '\'' + 
			",policestation = '" + policestation + '\'' + 
			",email = '" + email + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.licenseno);
		dest.writeString(this.licensefrontimg);
		dest.writeString(this.address);
		dest.writeString(this.aadharbackimg);
		dest.writeString(this.city);
		dest.writeString(this.licensevalidity);
		dest.writeString(this.mobile);
		dest.writeString(this.driverimg);
		dest.writeString(this.aadharno);
		dest.writeString(this.experience);
		dest.writeString(this.token);
		dest.writeString(this.licensecategory);
		dest.writeString(this.travelsid);
		dest.writeString(this.licensebackimg);
		dest.writeString(this.district);
		dest.writeString(this.name);
		dest.writeString(this.aadharfrontimg);
		dest.writeString(this.id);
		dest.writeString(this.state);
		dest.writeString(this.policestation);
		dest.writeString(this.email);
		dest.writeString(this.status);
	}

	public void readFromParcel(Parcel source) {
		this.licenseno = source.readString();
		this.licensefrontimg = source.readString();
		this.address = source.readString();
		this.aadharbackimg = source.readString();
		this.city = source.readString();
		this.licensevalidity = source.readString();
		this.mobile = source.readString();
		this.driverimg = source.readString();
		this.aadharno = source.readString();
		this.experience = source.readString();
		this.token = source.readString();
		this.licensecategory = source.readString();
		this.travelsid = source.readString();
		this.licensebackimg = source.readString();
		this.district = source.readString();
		this.name = source.readString();
		this.aadharfrontimg = source.readString();
		this.id = source.readString();
		this.state = source.readString();
		this.policestation = source.readString();
		this.email = source.readString();
		this.status = source.readString();
	}

	public DriverItem() {
	}

	protected DriverItem(Parcel in) {
		this.licenseno = in.readString();
		this.licensefrontimg = in.readString();
		this.address = in.readString();
		this.aadharbackimg = in.readString();
		this.city = in.readString();
		this.licensevalidity = in.readString();
		this.mobile = in.readString();
		this.driverimg = in.readString();
		this.aadharno = in.readString();
		this.experience = in.readString();
		this.token = in.readString();
		this.licensecategory = in.readString();
		this.travelsid = in.readString();
		this.licensebackimg = in.readString();
		this.district = in.readString();
		this.name = in.readString();
		this.aadharfrontimg = in.readString();
		this.id = in.readString();
		this.state = in.readString();
		this.policestation = in.readString();
		this.email = in.readString();
		this.status = in.readString();
	}

	public static final Parcelable.Creator<DriverItem> CREATOR = new Parcelable.Creator<DriverItem>() {
		@Override
		public DriverItem createFromParcel(Parcel source) {
			return new DriverItem(source);
		}

		@Override
		public DriverItem[] newArray(int size) {
			return new DriverItem[size];
		}
	};
}