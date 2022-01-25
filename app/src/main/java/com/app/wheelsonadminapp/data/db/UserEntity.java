package com.app.wheelsonadminapp.data.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


/* Database user table */
@Entity(tableName = "user")
public class UserEntity implements Serializable {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "ownerName")
    public String ownerName;

    @ColumnInfo(name = "vehicleNos")
    public String vehicleCount;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "travelsName")
    public String travelsName;

    @ColumnInfo(name = "mobile")
    public String mobile;

    @ColumnInfo(name = "userId")
    public String userId;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "token")
    public String token;


    public UserEntity() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(String vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTravelsName() {
        return travelsName;
    }

    public void setTravelsName(String travelsName) {
        this.travelsName = travelsName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "uid=" + uid +
                ", ownerName='" + ownerName + '\'' +
                ", vehicleCount='" + vehicleCount + '\'' +
                ", image='" + image + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", travelsName='" + travelsName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
