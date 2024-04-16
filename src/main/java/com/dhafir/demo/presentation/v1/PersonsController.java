package com.dhafir.demo.presentation.v1;


import com.dhafir.demo.data.Location;
import com.dhafir.demo.data.LongLat;
import com.dhafir.demo.data.Person;
import com.dhafir.demo.domain.services.LocationsService;
import com.dhafir.demo.domain.services.PersonsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/persons")
public class PersonsController {

    private PersonsService personsService;
    private LocationsService locationsServic;

    /*
        TODO PUT API to update/create someone's location using latitude and longitude
        (JSON) Body
     */

    /**
     * Adds/updates persons (identified by personId) location (identified by LongLat)
     * @param personId
     * @param longLat
     */
    @PutMapping("/{personId}/locations")
    public ResponseEntity updatePersonLocation(@PathVariable long personId, @RequestBody LongLat longLat) {
        // Sanity check 1: validate LongLat
        if (longLat == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Sanity check 2: find the person first
        Person person = personsService.getById(personId);
        if(person == null) {
            return errorResponse("No person was found with id: " + personId);
        }

        // see if location exist!
        Location location = new Location();
        location.setLongitude(longLat.getLongitude());
        location.setLatitude(longLat.getLatitude());
        location.setPerson(person);

        locationsServic.addLocation(location);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves persons location
     * @param personId
     * @return
     */
    @GetMapping("/{personId}/locations")
    public ResponseEntity<LongLat> getPersonLocation(@PathVariable long personId) {
        // Sanity check 1: find the person first
        Person person = personsService.getById(personId);
        if(person == null) {
            return errorResponse("No person was found with id: " + personId);
        }

        Location location = locationsServic.findLocation(personId);
        if(location == null) {
            return errorResponse("No location was found for a person with id: " + personId);
        }

        locationsServic.addLocation(location);
        return ResponseEntity.ok().body( LongLat.of(location));
    }

    /*
        TODO POST API to create a 'person'
        (JSON) Body and return the id of the created entity
    */
    @PostMapping
    public ResponseEntity<Long> createPerson(@RequestBody String name) {
        Person person = new Person();
        person.setName(name);
        Person newPerson = personsService.save(person);
        if (newPerson == null) {
            return errorResponse("Couldn't create a new person.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newPerson.getId());
    }

    /*
        TODO GET API to retrieve people around query location with a radius in KM, Use query param for radius.
        TODO API just return a list of persons ids (JSON)
        // Example
        // John wants to know who is around his location within a radius of 10km
        // API would be called using John's id and a radius 10km
     */
    @GetMapping("/{personId}/people-around")
    public ResponseEntity<List<Long>> findPersonsAround(@PathVariable long personId, @RequestParam double radius) {
        // Sanity check. have to retrieve Person by Id first.
        Person person = personsService.getById(personId);
        if (person == null || person.getLocation() == null) {
            return errorResponse("No person was found with id: " + personId);
        }

        List<Location> locations = locationsServic.findAround(person.getLocation().getLatitude(), person.getLocation().getLongitude(), radius);
        if(CollectionUtils.isEmpty(locations)) {
            return errorResponse(String.format("No person was found near person %f within a radius of %f Km",personId, radius));
        }

        List<Long> personsIds = locations.stream()
                .map(Location::getReferenceId)
                .filter(id -> id != personId) // remove our own location
                .collect(Collectors.toList());

        return  ResponseEntity.ok().body(personsIds);

    }

    /*
        TODO GET API to retrieve a person or persons name using their ids
        // Example
        // John has the list of people around them, now they need to retrieve everybody's names to display in the app
        // API would be called using person or persons ids
     */

    /**
     * Retrieves persons name given person id.
     * @param personId
     * @return
     */
    @GetMapping("/{personId}")
    public ResponseEntity<String> findPersonById(@PathVariable Long personId) {
        Person person = personsService.getById(personId);
        if(person == null) {
            return errorResponse("No person was found with id: " + personId);
        }

        return  ResponseEntity.ok().body(person.getName());
    }

    /**
     * Retrieves person names by their ids as a comma delimited request parameter.
     * Example: ?ids=1000,1010,1200
     * @param ids
     * @return
     */
    @GetMapping
    public ResponseEntity<List<String>> getPersonsNameByIds(@RequestParam String ids) {
        //Ids is a comma delimited string of person Ids
        List<Long> personsIds = Arrays.stream(ids.split(",")).map(id -> Long.valueOf(id)).collect(Collectors.toList());
        List<Person> persons = personsService.getByIds(personsIds);
        if(CollectionUtils.isEmpty(persons)) {
            return errorResponse("No person was found with any of these Ids: " + ids );
        }

        List<String> names = persons.stream().map(person -> person.getName()).collect(Collectors.toList());
        return  ResponseEntity.ok().body(names);
    }


    @GetMapping("/hello")
    public String getExample() {
        return "Hello Example";
    }

    private ResponseEntity errorResponse(String errorMessage) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}
