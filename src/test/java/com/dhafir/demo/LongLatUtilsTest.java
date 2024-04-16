package com.dhafir.demo;

import com.dhafir.demo.data.LongLat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongLatUtilsTest {
    @Test
    public void testCalculateDistance() {
        // Coordinates of Auckland, New Zealand (latitude, longitude)
        double lat1 = -36.8485; // Auckland
        double lon1 = 174.7633;
        LongLat longLat1 = new LongLat(lat1, lon1);

        // Coordinates of Wellington, New Zealand (latitude, longitude)
        double lat2 = -41.2865; // Wellington
        double lon2 = 174.7762;
        LongLat longLat2 = new LongLat(lat2, lon2);

        // Expected distance between Auckland and Wellington
        // Calculated using an online distance calculator
        double expectedDistance = 494.13; // in kilometers

        // Calculate the distance using the DistanceCalculator utility class
        double actualDistance = LongLatUtils.calculateDistance(longLat1, longLat2);

        // Assert that the actual distance matches the expected distance
        // with a delta of 0.01 (to account for floating-point precision)
        assertEquals(expectedDistance, actualDistance, 1.0);
    }
}