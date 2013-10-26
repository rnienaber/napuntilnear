package com.therandomist.nap.service;

import android.location.Location;
import com.therandomist.nap.MagicNumber;
import com.therandomist.nap.model.Destination;

public class DistanceCalculationService {

    public Location currentDestination;

    public DistanceCalculationService(Location currentDestination) {
        if(currentDestination == null) throw new IllegalArgumentException("Current Destination must not be null.");
        this.currentDestination = currentDestination;
    }

    public DistanceCalculationService(Destination destination){
        if(destination == null) throw new IllegalArgumentException("Current Destination must not be null.");
        Location location = new Location("");
        location.setLatitude(destination.getLatitude());
        location.setLongitude(destination.getLongitude());
        this.currentDestination = location;
    }

    public double distanceBetween(Location startLocation, Location endLocation) {
        double R = 6371; // km
        double dLat = Math.toRadians(endLocation.getLatitude() -startLocation.getLatitude());
        double dLon = Math.toRadians(endLocation.getLongitude()-startLocation.getLongitude());
        double radLat1 = Math.toRadians(startLocation.getLatitude());
        double radLat2 = Math.toRadians(endLocation.getLatitude());

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(radLat1) * Math.cos(radLat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;

        return distance;
    }

    public boolean isCloseToDestination(Location currentLocation){
        double distance = distanceBetween(currentLocation, currentDestination);
        return distance <= MagicNumber.CLOSE_TO_DESTINATION;
    }

    public double getDistanceToDestination(Location currentLocation) {
        return distanceBetween(currentLocation, currentDestination);
    }
}

