package com.dhafir.demo.domain.services;


import com.dhafir.demo.data.Location;
import com.dhafir.demo.data.LongLat;
import com.dhafir.demo.datastore.LocationRepository;
import com.dhafir.demo.types.Latitude;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.dhafir.demo.LongLatUtils.calculateDistance;
import static com.fasterxml.jackson.databind.type.LogicalType.Collection;


@Slf4j
@AllArgsConstructor
@Service
public class LocationsServiceImpl implements LocationsService {

    private LocationRepository locationRepository;

    @Override
    public void addLocation(Location location) {
        log.info("Adding location with long longitude {} and latitude {}",
                location.getLongitude(),
                location.getLatitude());
        locationRepository.save(location);
    }

    @Override
    public void removeLocation(long locationId) {
        locationRepository.deleteById(locationId);
    }

    @Override
    public List<Location> findAround(double latitude, double longitude, double radiusInKm) {
        List<Location> locationsAround = locationRepository.findLocationsAround(latitude, longitude, radiusInKm);
        // Create a custom comparator based on distance between locations
        LongLat referenceLocation = new LongLat(latitude, longitude);
        Comparator<Location> distanceComparator = Comparator.comparingDouble(location -> calculateDistance(LongLat.of(location), referenceLocation));
        Collections.sort(locationsAround, distanceComparator);
        return locationsAround;
    }

}
