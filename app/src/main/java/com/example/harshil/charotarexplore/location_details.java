package com.example.harshil.charotarexplore;

/**
 * Created by harshil on 21-01-2017.
 */

public class location_details {
    private static String latitude;
    private static String longitude;
    
    public static String getLatitude() {
        return latitude;
    }

    public static void setLatitude(String latitude) {
        location_details.latitude = latitude;
    }

    public static String getLongitude() {
        return longitude;
    }

    public static void setLongitude(String longitude) {
        location_details.longitude = longitude;
    }
}
