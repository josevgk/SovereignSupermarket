package com.sovereign.supermarket.model;

import java.util.List;

/**
 * Created by Jose on 07/06/2017.
 */

public class Supermarket {

    private String name;
    private String latitude;
    private String longitude;
    private String keySupermarket;
    private String address;

    public Supermarket(String name, String latitude, String longitude,String keySupermarket, String address) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.keySupermarket=keySupermarket;
        this.address=address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getKeySupermarket() {
        return keySupermarket;
    }

    public void setKeySupermarket(String keySupermarket) {
        this.keySupermarket = keySupermarket;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "Supermarket{" +
                "name='" + name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude +
                '}';
    }
}
