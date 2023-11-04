package com.example.demo.stream.people.utils;

import com.example.demo.stream.people.model.Person;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CsvReadFile implements ReadFile {
    private final ResourceLoader resourceLoader;

    public CsvReadFile(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public List<Person> readFile(String filePath) {
        List<Person> people = new ArrayList<>();

        Resource resource = resourceLoader.getResource(filePath);
        try {
            FileReader filereader = new FileReader(resource.getFile());
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();

            List<String[]> allData = csvReader.readAll();
            for (String[] row : allData) {
                Person person = new Person();
                person.setId(Integer.valueOf(row[0]));
                person.setFullname(row[1]);
                person.setJob(row[2]);
                person.setGender(row[3]);
                person.setCity(row[4]);
                person.setSalary(Integer.valueOf(row[5]));
                person.setBirthday(row[6]);

                people.add(person);
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }

        return people;
    }
}
