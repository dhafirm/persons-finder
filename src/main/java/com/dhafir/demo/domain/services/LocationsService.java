package com.dhafir.demo.domain.services;


import com.dhafir.demo.data.Location;

import java.util.List;

public interface LocationsService {
    void addLocation(Location location);
    void removeLocation(long locationId);
    Location findLocation(long id);

    List<Location> findAround(double latitude, double longitude, double radiusInKm);
}
