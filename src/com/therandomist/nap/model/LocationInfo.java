package com.therandomist.nap.model;

import android.location.Location;

public class LocationInfo {

    private Location location;
    private String provider;

    public LocationInfo(Location location, String provider) {
        this.location = location;
        this.provider = provider;
    }

    public Location getLocation() {
        return location;
    }

    public String getProvider() {
        return provider;
    }

    public double getLatitude(){
        if(location != null) return location.getLatitude();
        return 0;
    }

    public double getLongitude(){
        if(location != null) return location.getLongitude();
        return 0;
    }
}
