package com.dhafir.demo.presentation.v1;

import com.dhafir.demo.data.Location;
import com.dhafir.demo.data.LongLat;
import com.dhafir.demo.data.Person;
import com.dhafir.demo.domain.services.LocationsService;
import com.dhafir.demo.domain.services.PersonsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonsControllerTest {

    @Mock
    private PersonsService personsServiceMock;

    @Mock
    private LocationsService locationsServiceMock;

    @InjectMocks
    private PersonsController personsController;
    @Test
    void updatePersonLocation_shouldReturnErro404IfPersonNotFound() {
        when(personsServiceMock.getById(1000L)).thenReturn(null);

        double latitude = 74.1111;
        double longitude = 80.2222;
        ResponseEntity response = personsController.updatePersonLocation(1000L, new LongLat(latitude, longitude));

        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void updatePersonLocation_shouldReturnOkIfPersonExists() {
        Person person = new Person();
        person.setId(1000L);
        when(personsServiceMock.getById(1000L)).thenReturn(person);

        Location location = new Location();
        double latitude = 74.1111;
        double longitude = 80.2222;
        ResponseEntity response = personsController.updatePersonLocation(1000L, new LongLat(latitude, longitude));

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    void createPerson_shouldReturnOk() {
        Person savedPerson = new Person();
        savedPerson.setId(1000L);
        Mockito.lenient().when(personsServiceMock.save(any(Person.class))).thenReturn(savedPerson);

        ResponseEntity response = personsController.createPerson(any(String.class));

        verify(personsServiceMock).save(any(Person.class));
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

    }

    @Test
    void getPeopleAround_shouldReturnErro404IfPersonNotFound() {
        when(personsServiceMock.getById(1000L)).thenReturn(null);
        ResponseEntity response = personsController.findPersonsAround(1000L, 20);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void getPeopleAround_shouldReturnOkAndExcludesOurtOwnLocation() {
        double latitude = 74.1111;
        double longitude = 80.2222;

        Person person = new Person();
        person.setId(1000L);
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setId(1000L);
        person.setLocation(location);

        Location location1 = new Location();
        location1.setId(1001L);

        Location location2 = new Location();
        location2.setId(1002L);

        Location location3 = new Location();
        location3.setId(1003L);

        List<Location> locations = new ArrayList<>();
        locations.add(location);
        locations.add(location1);
        locations.add(location2);
        locations.add(location3);

        when(locationsServiceMock.findAround(latitude, longitude, 10)).thenReturn(locations);
        when(personsServiceMock.getById(1000L)).thenReturn(person);

        ResponseEntity response = personsController.findPersonsAround(1000L, 10);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        List<Long> body = (List<Long>)response.getBody();
        assertFalse(body.contains(location));// shoudl remove our own location from the list
    }

    @Test
    void findPersonById_shouldReturn404IfPersonNotFound() {
        when(personsServiceMock.getById(1000L)).thenReturn(null);
        ResponseEntity response = personsController.findPersonById(1000L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void findPersonById_shouldReturnOkIfPersonExists() {
        Person person = new Person();
        person.setId(1000L);
        person.setName("John");
        when(personsServiceMock.getById(1000L)).thenReturn(person);
        ResponseEntity response = personsController.findPersonById(1000L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertEquals(response.getBody().toString(), "John");
    }

    @Test
    void getPersonsNameByIds_shouldReturn404IfNoneExist() {
        when(personsServiceMock.getByIds(List.of(1000L, 2000L, 3000L))).thenReturn(List.of());
        ResponseEntity response = personsController.getPersonsNameByIds("1000,2000,3000");
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void getPersonsNameByIds_shouldReturnOkIfAnyPersonFound() {
        Person person = new Person();
        person.setName("Nirav");
        person.setId(1000L);
        when(personsServiceMock.getByIds(List.of(1000L, 2000L, 3000L))).thenReturn(List.of(person));
        ResponseEntity response = personsController.getPersonsNameByIds("1000,2000,3000");
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        List<String> body = (List<String>)response.getBody();
        assertTrue(body.contains("Nirav"));
    }
}