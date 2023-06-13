package com.example.demo.utils;

import com.example.demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component("ExcelReadFile")
@Slf4j
public class ExcelReadFile implements ReadFile {
    private final ResourceLoader resourceLoader;

    public ExcelReadFile(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public List<User> readFile(String filePath) {
        List<User> userList = new ArrayList<>();

        Resource resource = resourceLoader.getResource(filePath);
        try {
            InputStream inputStream = resource.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assuming you want to read the first sheet

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Cell idCell = row.getCell(0);
                Cell nameCell = row.getCell(1);
                Cell emailCell = row.getCell(2);
                Cell ageCell = row.getCell(3);

                User user = new User();
                user.setId((int) idCell.getNumericCellValue());
                user.setName(nameCell.getStringCellValue());
                user.setEmail(emailCell.getStringCellValue());
                user.setAge((int) ageCell.getNumericCellValue());

                userList.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }
}
