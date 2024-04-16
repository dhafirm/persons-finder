package com.dhafir.demo;

import com.dhafir.demo.data.LongLat;

/**
 * Utility class to handle Long/Lat calculations
 */
public class LongLatUtils {
    // Radius of the Earth in kilometers
    private static final double EARTH_RADIUS_KM = 6371.0;

    // Convert degrees to radians
    private static double toRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    // Calculate distance using Haversine formula
    public static double calculateDistance(LongLat loc1, LongLat loc2) {
        double dLat = toRadians(loc1.getLatitude() - loc2.getLatitude());
        double dLon = toRadians(loc1.getLongitude() - loc2.getLongitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(toRadians(loc1.getLongitude())) * Math.cos(toRadians(loc2.getLongitude())) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}
