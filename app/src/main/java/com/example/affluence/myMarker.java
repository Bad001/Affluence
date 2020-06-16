package com.example.affluence;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class myMarker implements Parcelable {
    private String id;
    private double longitude;
    private double latitude;

    public myMarker(double latitude, double longitude) {
        this.id = createId();
        this.longitude = longitude;
        this.latitude = latitude;
    }

    protected myMarker(Parcel in) {
        id = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<myMarker> CREATOR = new Creator<myMarker>() {
        @Override
        public myMarker createFromParcel(Parcel in) {
            return new myMarker(in);
        }

        @Override
        public myMarker[] newArray(int size) {
            return new myMarker[size];
        }
    };

    public String getId() {
        return id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String createId() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        while (sb.length() < 12) { // length of the random string.
            int index = (int) (rnd.nextFloat() * characters.length());
            sb.append(characters.charAt(index));
        }
        String result = sb.toString();
        return result;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }
}