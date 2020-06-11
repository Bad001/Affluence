package com.example.affluence;

import java.util.Random;

public class myMarker {
    private String id;
    private double longitude;
    private double latitude;

    public myMarker(double latitude, double longitude) {
        this.id = createId();
        this.longitude = longitude;
        this.latitude = latitude;
    }

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
}