package com.dhafir.demo.datastore;


import com.dhafir.demo.data.Location;
import com.dhafir.demo.data.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PersonRepository extends JpaRepository<Person, Long> {
    /**
     * Retrieves a List of Persons given a Set of their Ids.
     * @param ids
     * @return
     */
    @Query("SELECT p FROM Person p  WHERE p.id in :ids")
    List<Person> findPersonsByIds(List<Long> ids);
}

