package com.example.demo.stream.people.repository;

import com.example.demo.stream.people.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonRepositoryImplTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    void getAll() {
        personRepository.getAll().forEach(System.out::println);
    }

    @Test
    void sortPeopleByFullName() {
        personRepository.sortPeopleByFullName().forEach(System.out::println);
    }

    @Test
    void sortPeopleByFullNameReversed() {
        personRepository.sortPeopleByFullNameReversed().forEach(System.out::println);
    }

    @Test
    void getSortedJobs() {
        personRepository.getSortedJobs().forEach(System.out::println);
    }

    @Test
    void getSortedCities() {
        personRepository.getSortedCities().forEach(System.out::println);
    }

    @Test
    void groupPeopleByCity() {
        Map<String, List<Person>> map = personRepository.groupPeopleByCity();
        map.forEach((key, value) -> {
            System.out.println(key);
            value.forEach(System.out::println);
        });
    }

    @Test
    void groupJobByCount() {
        personRepository.groupJobByCount().forEach((key, value) -> System.out.println(key + " - " + value));
    }

    @Test
    void findTop5Jobs() {
        personRepository.findTop5Jobs().forEach((key, value) -> System.out.println(key + " - " + value));
    }

    @Test
    void findTop5PopulationCities() {
        personRepository.findTop5PopulationCities().forEach(System.out::println);
    }

    @Test
    void findTopJobInCity() {
        List<Map.Entry<String, Map.Entry<String, Long>>> list = personRepository.findTopJobInCity();
        list.forEach(System.out::println);
    }

    @Test
    void averageJobSalary() {
        personRepository.averageJobSalary().forEach((key, value) -> System.out.println(key + " - " + value));
    }

    @Test
    void top5HighestSalaryCities() {
        personRepository.top5HighestSalaryCities().forEach(System.out::println);
    }

    @Test
    void averageJobAge() {
        personRepository.averageJobAge().forEach((key, value) -> System.out.println(key + " - " + value));
    }

    @Test
    void ratioMaleFemalePerCity() {
        personRepository.ratioMaleFemalePerCity().forEach((key, value) -> System.out.println(key + " - " + value));
    }

    @Test
    void averageCityAge() {
        personRepository.averageCityAge().forEach((key, value) -> System.out.println(key + " - " + value));
    }

    @Test
    void find5CitiesHaveMostSpecificJob() {
        personRepository.find5CitiesHaveMostSpecificJob("Developer").forEach(System.out::println);
    }
}