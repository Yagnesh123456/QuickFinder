package com.saubhagyam.quickfinderplaces;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "mytable")

public class DatabasePojo {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "PlaceName")
    private String Placename;

    @ColumnInfo(name = "Place Address")
    private String Address;

    @ColumnInfo(name = "Rating")
    private String Rating;

    @ColumnInfo(name = "Place Icon")
    private String placeicon;

    @ColumnInfo(name = "km")
    private String km;

    @ColumnInfo(name = "status")
    private String status;


    public DatabasePojo() {

    }

    public DatabasePojo(String placename, String address) {
        Placename = placename;
        Address = address;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPlacename() {
        return Placename;
    }

    public void setPlacename(String placename) {
        Placename = placename;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getPlaceicon() {
        return placeicon;
    }

    public void setPlaceicon(String placeicon) {
        this.placeicon = placeicon;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
