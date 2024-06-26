package com.dhafir.demo.presentation.v1;

import com.dhafir.demo.data.Location;
import com.dhafir.demo.data.LongLat;
import com.dhafir.demo.data.Person;
import com.dhafir.demo.domain.services.LocationsService;
import com.dhafir.demo.domain.services.PersonsService;
import com.dhafir.demo.presentation.dto.PersonDTO;
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
    void updateOrCreateLocation_shouldReturnErro404IfPersonNotFound() {
        when(personsServiceMock.getById(1000L)).thenReturn(null);

        double latitude = 74.1111;
        double longitude = 80.2222;
        ResponseEntity response = personsController.updateOrCreateLocation(1000L, new LongLat(latitude, longitude));

        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void updateOrCreateLocation_shouldReturnOkIfPersonExists() {
        Person person = new Person();
        person.setId(1000L);
        when(personsServiceMock.getById(1000L)).thenReturn(person);

        Location location = new Location();
        double latitude = 74.1111;
        double longitude = 80.2222;
        ResponseEntity response = personsController.updateOrCreateLocation(1000L, new LongLat(latitude, longitude));

        assertThat(response.getStatusCode(), equalTo(HttpStatus.ACCEPTED));
    }

    @Test
    void createPerson_shouldReturn400IfPersonNameIsMissing() {
        // given
        PersonDTO personDto = new PersonDTO();

        // when
        ResponseEntity response = personsController.createPerson(personDto);

        // assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }


    @Test
    void createPerson_shouldReturn400IfPersonNameIsEmpty() {
        // given
        PersonDTO personDto = new PersonDTO();
        personDto.setName("");

        // when
        ResponseEntity response = personsController.createPerson(personDto);

        // assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    void createPerson_shouldReturn422IfFailedToCreatePerson() {
        Person person = new Person();
        person.setId(1000L);
        Mockito.lenient().when(personsServiceMock.save(person)).thenReturn(null);

        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("John");
        ResponseEntity response = personsController.createPerson(personDTO);

        verify(personsServiceMock).save(any(Person.class));
        assertThat(response.getStatusCode(), equalTo(HttpStatus.UNPROCESSABLE_ENTITY));

    }

    @Test
    void createPerson_shouldReturnOk() {
        Person newPerson = new Person();
        newPerson.setId(1000L);
        Mockito.lenient().when(personsServiceMock.save(any(Person.class))).thenReturn(newPerson);

        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("John");
        ResponseEntity response = personsController.createPerson(personDTO);

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
        location.setReferenceId(1000L);
        person.setLocation(location);

        Location location1 = new Location();
        location1.setReferenceId(1001L);

        Location location2 = new Location();
        location2.setReferenceId(1002L);

        Location location3 = new Location();
        location3.setReferenceId(1003L);

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

    @Test
    void findLocation_shouldReturn404IfPersonNotFound() {
        when(personsServiceMock.getById(1000L)).thenReturn(null);
        ResponseEntity response = personsController.getPersonLocation(1000L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void findLocation_shouldReturn404IfLocationNotFound() {
        Person person = new Person();
        person.setId(1000L);
        when(personsServiceMock.getById(1000L)).thenReturn(person);
        when(locationsServiceMock.findLocation(1000L)).thenReturn(null);

        ResponseEntity response = personsController.getPersonLocation(1000L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void findLocation_shouldReturnOkIfBothPersonAndLocationFound() {
        Person person = new Person();
        person.setId(1000L);
        when(personsServiceMock.getById(1000L)).thenReturn(person);

        Location location = new Location();
        double latitude = -36.123456;
        double longitude = 174.773872;
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        when(locationsServiceMock.findLocation(1000L)).thenReturn(location);

        ResponseEntity response = personsController.getPersonLocation(1000L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        LongLat longLat = (LongLat) response.getBody();
        assertEquals(longLat.getLatitude(),latitude);
        assertEquals(longLat.getLongitude(),longitude);
    }
}