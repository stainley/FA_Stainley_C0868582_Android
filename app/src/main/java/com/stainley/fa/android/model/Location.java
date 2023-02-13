package com.stainley.fa.android.model;

import androidx.room.ColumnInfo;

public class Location {
    @ColumnInfo(name = "LATITUDE")
    private double latitude;
    @ColumnInfo(name = "LONGITUDE")
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
