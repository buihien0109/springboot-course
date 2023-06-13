package com.example.demo.utils;

import com.example.demo.model.User;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Component("CsvReadFile")
public class CsvReadFile implements ReadFile {
    private final ResourceLoader resourceLoader;

    public CsvReadFile(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public List<User> readFile(String filePath) {
        List<User> userList = new ArrayList<>();

        Resource resource = resourceLoader.getResource(filePath);
        try {
            FileReader filereader = new FileReader(resource.getFile());
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();

            List<String[]> allData = csvReader.readAll();
            for (String[] row : allData) {
                User user = new User();
                user.setId(Integer.valueOf(row[0]));
                user.setName(row[1]);
                user.setEmail(row[2]);
                user.setAge(Integer.valueOf(row[3]));

                userList.add(user);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }
}
