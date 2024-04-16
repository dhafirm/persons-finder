package com.dhafir.demo.domain.services;


import com.dhafir.demo.data.Person;

import java.util.List;
import java.util.Set;

public interface PersonsService {
    Person getById(Long id);
    Person save(Person person);

    List<Person> getByIds(List<Long> ids);
}
