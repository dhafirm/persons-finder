package com.dhafir.demo.datastore;

import com.dhafir.demo.data.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    /**
     * This query method will attempt to find all locations within a specified radius from certain location.
     * The location long/lat we have are in degrees across vertical and horizontal lines on the Earth, so we need special formula
     * to calculate a distance from certain long/lat (central point).
     * The ACOS function calculates the arc cosine of a value, which is used to compute the angular distance between two points on the Earth's surface.
     * The result of the calculation is multiplied by the Earth's radius (6371 kilometers) to get the distance in kilometers.
     *
     * @param centerLatitude
     * @param centerLongitude
     * @param radiusInKm
     * @return
     */
    @Query(value = "SELECT * FROM location l " +
            "WHERE " +
            "ACOS(SIN(RADIANS(?1)) * SIN(RADIANS(l.latitude)) " +
            "+ COS(RADIANS(?1)) * COS(RADIANS(l.latitude)) " +
            "* COS(RADIANS(l.longitude - ?2))) * 6371 <= ?3", nativeQuery = true)
    List<Location> findLocationsAround(double centerLatitude, double centerLongitude, double radiusInKm);
}
