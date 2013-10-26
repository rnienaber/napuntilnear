package com.therandomist.nap.service;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class LocationChangeListener extends Observable implements LocationListener {

    String locationProvider;
    Location currentLocation;
    Observer observer;

    public LocationChangeListener(String locationProvider, Observer observer) {
        this.locationProvider = locationProvider;
        this.observer = observer;
        Log.i("RACHEL", "Listening for locations from: "+locationProvider);
    }

    public void onLocationChanged(Location location) {
        this.currentLocation = location;
        Log.i("RACHEL", "Location changed from: "+locationProvider);
        observer.update(this, null);
    }

    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    public void onProviderEnabled(String s) {
    }

    public void onProviderDisabled(String s) {
    }

    public String getLocationProvider() {
        return locationProvider;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }
}
