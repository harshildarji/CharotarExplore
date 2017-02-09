package com.example.harshil.charotarexplore;

/**
 * Created by harshil on 30-01-2017.
 */

public class resultData {
    private String id, name, number, address, time, latitude, longitude, image, catid;

    public resultData(String id, String name, String number, String address, String time, String latitude, String longitude, String image, String catid) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.address = address;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.catid = catid;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getAddress() {
        return address;
    }

    public String getTime() {
        return time;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getImage() {
        return image;
    }

    public String getCatid() {
        return catid;
    }
}
