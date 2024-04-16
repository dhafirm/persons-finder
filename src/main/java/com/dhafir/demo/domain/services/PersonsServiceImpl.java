package com.dhafir.demo.domain.services;


import com.dhafir.demo.data.Person;
import com.dhafir.demo.datastore.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class PersonsServiceImpl implements PersonsService {
    @Autowired
    private PersonRepository personRepository;

    @Override
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Person getById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    @Override
    public List<Person> getByIds(List<Long> ids) {
        return personRepository.findPersonsByIds(ids);
    }
}
