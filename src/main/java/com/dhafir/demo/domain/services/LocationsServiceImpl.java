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
        Location savedLocation = locationRepository.save(location);
        log.info("Added location with longitude {} and latitude {} for person {}",
                savedLocation.getLongitude(),
                savedLocation.getLatitude(),
                savedLocation.getPerson().getName());
    }


    @Override
    public void removeLocation(long locationId) {
        locationRepository.deleteById(locationId);
        log.info("Removed location  {}", locationId);
    }

    @Override
    public Location findLocation(long id) {
        return locationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Location> findAround(double latitude, double longitude, double radiusInKm) {
        log.info("Finding all locations around the given location within a radius of {} Km", radiusInKm);
        List<Location> locationsAround = locationRepository.findLocationsAround(latitude, longitude, radiusInKm);
        // Create a custom comparator based on distance between locations
        LongLat referenceLocation = new LongLat(latitude, longitude);
        Comparator<Location> distanceComparator = Comparator.comparingDouble(location -> calculateDistance(LongLat.of(location), referenceLocation));
        Collections.sort(locationsAround, distanceComparator);
        log.info("Found {} locations around.", locationsAround.size() - 1);// exclude our own location
        return locationsAround;
    }

}
