package com.example.demo.stream.people.repository;

import com.example.demo.stream.people.model.Person;
import com.example.demo.stream.people.utils.ReadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PersonRepositoryImpl implements PersonRepository {
    private List<Person> people;

    @Autowired
    public PersonRepositoryImpl(ReadFile fileReader) {
        this.people = fileReader.readFile("classpath:static/people.csv");
    }

    @Override
    public void printListPeople(List<Person> persons) {
        persons.forEach(System.out::println);
    }

    @Override
    public List<Person> getAll() {
        return people;
    }

    @Override
    public List<Person> sortPeopleByFullName() {
        return people.stream()
                .sorted(Comparator.comparing(Person::getFullname))
                .toList();
    }

    @Override
    public List<Person> sortPeopleByFullNameReversed() {
        return people.stream()
                .sorted(Comparator.comparing(Person::getFullname).reversed())
                .toList();
    }

    @Override
    public List<String> getSortedJobs() {
        return people.stream()
                .map(Person::getJob)
                .distinct()
                .sorted()
                .toList();
    }

    @Override
    public List<String> getSortedCities() {
        return people.stream()
                .map(Person::getCity)
                .distinct()
                .sorted()
                .toList();
    }

    @Override
    public Map<String, List<Person>> groupPeopleByCity() {
        return people.stream()
                .collect(Collectors.groupingBy(Person::getCity));
    }

    @Override
    public Map<String, Integer> groupJobByCount() {
        return people.stream()
                .collect(Collectors.groupingBy(Person::getJob, Collectors.summingInt(e -> 1)));
    }

    @Override
    public HashMap<String, Integer> findTop5Jobs() {
        return people.stream()
                .collect(Collectors.groupingBy(Person::getJob, Collectors.summingInt(e -> 1)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, HashMap::new));
    }

    @Override
    public List<Map.Entry<String, Integer>> findTop5PopulationCities() {
        return people.stream()
                .collect(Collectors.groupingBy(Person::getCity, Collectors.summingInt(e -> 1)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .toList();
    }

    @Override
    public List<Map.Entry<String, Map.Entry<String, Long>>> findTopJobInCity() {
        return people.stream()
                .collect(Collectors.groupingBy(Person::getCity, Collectors.groupingBy(Person::getJob, Collectors.counting())))
                .entrySet()
                .stream()
                .map(e -> Map.entry(e.getKey(), e.getValue().entrySet().stream().max(Map.Entry.comparingByValue()).get()))
                .toList();
    }

    @Override
    public Map<String, Double> averageJobSalary() {
        return people.stream()
                .collect(Collectors.groupingBy(Person::getJob, Collectors.averagingDouble(Person::getSalary)));
    }

    @Override
    public List<Map.Entry<String, Double>> top5HighestSalaryCities() {
        return people.stream()
                .collect(Collectors.groupingBy(Person::getCity, Collectors.averagingDouble(Person::getSalary)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .toList();
    }

    @Override
    public Map<String, Double> averageJobAge() {
        return people.stream()
                .collect(Collectors.groupingBy(Person::getJob, Collectors.averagingInt(Person::getAge)));
    }

    @Override
    public HashMap<String, Float> ratioMaleFemalePerCity() {
        // Tính tỷ lệ Nam/Nữ cho mỗi thành phố
        HashMap<String, Float> ratioMaleFemalePerCity = new HashMap<>();

        // Tính tổng số lượng nam và nữ cho mỗi thành phố
        Map<String, Map<String, Long>> countMaleFemalePerCity = people.stream()
                .collect(Collectors.groupingBy(Person::getCity, Collectors.groupingBy(Person::getGender, Collectors.counting())));

        // Tính tỷ lệ nam/nữ cho mỗi thành phố
//        countMaleFemalePerCity.forEach((city, countMaleFemale) -> {
//            Float ratioMaleFemale = countMaleFemale.get("");
//            ratioMaleFemalePerCity.put(city, ratioMaleFemale);
//        });

        return ratioMaleFemalePerCity;
    }

    @Override
    public Map<String, Double> averageCityAge() {
        return people.stream()
                .collect(Collectors.groupingBy(Person::getCity, Collectors.averagingInt(Person::getAge)));
    }

    @Override
    public List<String> find5CitiesHaveMostSpecificJob(String job) {
        return people.stream()
                .filter(person -> person.getJob().equals(job))
                .collect(Collectors.groupingBy(Person::getCity, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
    }
}
