package com.dhafir.demo.domain.services;

import com.dhafir.demo.data.Person;
import com.dhafir.demo.datastore.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonsServiceImplTest {

    @Mock
    private PersonRepository personRepositoryMock;
    @InjectMocks
    private PersonsServiceImpl personsService;

    @Test
    void save_shouldSaveNewPersonAndReturnSavedEntity() {
        Person person = new Person();
        person.setName("John");

        Person newPerson = new Person();
        newPerson.setName("John");
        newPerson.setId(1000L);

        when(personRepositoryMock.save(person)).thenReturn(newPerson);
        Person savedPerson = personsService.save(person);

        verify(personRepositoryMock).save(person);

        assertEquals(savedPerson.getId(), 1000L);

    }

    @Test
    void getById_shouldReturnPersonEntityGivenPErsonId() {
        Person person = new Person();
        person.setName("John");
        person.setId(1000L);
        Optional<Person> maybePerson = Optional.of(person);
        when(personRepositoryMock.findById(1000L)).thenReturn(maybePerson);
        Person returnedPerson = personsService.getById(1000L);

        assertThat(returnedPerson.getId(), equalTo(1000L));
        assertThat(returnedPerson.getName(), equalTo("John"));
    }

    @Test
    void getByIds_shouldReturnAllPersonsWIthGivenIds() {
        Person person1 = new Person();
        person1.setName("John");
        person1.setId(1000L);

        Person person2 = new Person();
        person2.setName("Martin");
        person2.setId(2000L);

        List<Person> persons = List.of(person1, person2);
        when(personRepositoryMock.findPersonsByIds(List.of(1000L, 2000L))).thenReturn(persons);

        List<Person> returnedPersons = personsService.getByIds(List.of(1000L, 2000L));

        assertEquals(returnedPersons.size(), 2);
        assertTrue(returnedPersons.contains(person1));
        assertTrue(returnedPersons.contains(person2));
    }
}