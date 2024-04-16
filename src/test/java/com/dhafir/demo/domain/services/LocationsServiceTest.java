package com.dhafir.demo.domain.services;

import com.dhafir.demo.data.Location;
import com.dhafir.demo.datastore.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationsServiceTest {

    @Mock
    private LocationRepository locationRepositoryMock;

    @InjectMocks
    private LocationsServiceImpl locationsService;

    @Test
    void addLocation() {
        Double latitude = 1.2345678D;
        Double longitude = 2.987654D;
        Location location = new Location();
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        when(locationRepositoryMock.save(location)).thenReturn(location);

        locationsService.addLocation(location);

        verify(locationRepositoryMock).save(location);
    }

    @Test
    void removeLocation() {
        locationsService.removeLocation(1000L);
        verify(locationRepositoryMock).deleteById(1000L);
    }

    @Test
    void findAround() {
        Location loc1 = new Location();
        loc1.setLatitude(36.1111);
        loc1.setLongitude(174.1111);

        Location loc2 = new Location();
        loc2.setLatitude(36.2222);
        loc2.setLongitude(174.2222);

        Location loc3 = new Location();
        loc3.setLatitude(36.3333);
        loc3.setLongitude(174.3333);

        List<Location> locations = new ArrayList();
        locations.add(loc1);
        locations.add(loc2);
        locations.add(loc3);

        when(locationRepositoryMock.findLocationsAround(36.9999, 174.9999, 20.5)).thenReturn(locations);

        List<Location> locationsAround  = locationsService.findAround(36.9999, 174.9999, 20.5);

        verify(locationRepositoryMock).findLocationsAround(36.9999, 174.9999, 20.5);
        assertEquals(locations.size(), 3);
        assertThat(locationsAround.get(0), equalTo(loc3));
        assertThat(locationsAround.get(1), equalTo(loc2));
        assertThat(locationsAround.get(2), equalTo(loc1));
    }
}