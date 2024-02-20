package org.example.demo.utils.file_utils;

import org.example.demo.model.Product;

import java.util.List;

public interface IFileReader {
    List<Product> readFile(String filePath);
}
