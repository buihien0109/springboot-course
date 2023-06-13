package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class DemoStreamApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoStreamApiApplication.class, args);

        // Demo sử dụng forEach và stream API khi thực hiện 1 bài tập
        // BT : Cho 1 danh sách số nguyên. Tìm các số chẵn có trong danh sách đó
        List<Integer> numbers = List.of(10, 5, 3, 12, 6, 7, 5, 3);

        // Cách 1 : Sử dụng forEach
        List<Integer> listEvenNumber = new ArrayList<>();
        numbers.forEach(number -> {
            if(number % 2 == 0) {
                listEvenNumber.add(number);
            }
        });
        System.out.println(listEvenNumber);

        // Cách 2 : Sử dụng stream API
        List<Integer> listEvenNumber1 = numbers.stream()
                .filter(number -> number % 2 == 0)
                .toList();
        System.out.println(listEvenNumber1);

        // MỘT SỐ VÍ DỤ KHÁC SỬ DỤNG STREAM API
        // Duyệt numbers
        numbers.stream().forEach(System.out::println);
        System.out.println();

        // Tìm các giá trị chẵn trong list
        numbers.stream().filter(n -> n % 2 == 0).forEach(System.out::println);
        System.out.println();

        // Tìm các giá trị > 5 trong list
        numbers.stream().filter(n -> n > 5).forEach(System.out::println);
        System.out.println();

        // Tìm giá trị max trong list
        Optional<Integer> maxOptional = numbers.stream().max((a, b) -> a - b);
        maxOptional.ifPresent(System.out::println);
        System.out.println();

        numbers.stream()
                .sorted(Comparator.comparing(a -> (int) a).reversed())
                .limit(1)
                .forEach(System.out::println);
        System.out.println();

        // Tìm giá trị min trong list
        Optional<Integer> minOptional = numbers.stream().min((a, b) -> a - b);
        minOptional.ifPresent(System.out::println);
        System.out.println();

        numbers.stream()
                .sorted(Comparator.comparing(a -> (int) a))
                .limit(1)
                .forEach(System.out::println);
        System.out.println();

        // Tính tổng các phần tử của mảng
        int sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println(sum + "\n");

        // Lấy danh sách các phần tử không trùng nhau
        numbers.stream().distinct().forEach(System.out::println);
        System.out.println();

        // Lấy 5 phần tử đầu tiên trong mảng
        numbers.stream().limit(5).forEach(System.out::println);
        System.out.println();

        // Lấy phần tử từ thứ 3 -> thứ 5
        numbers.stream().skip(2).limit(3).forEach(System.out::println);
        System.out.println();

        // Lấy phần tử đầu tiên > 5
        Optional<Integer> optionalFirstElementGreater5 = numbers.stream().filter(n -> n > 5).findFirst();
        optionalFirstElementGreater5.ifPresent(System.out::println);

        // Kiểm tra xem list có phải là list chẵn hay không
        boolean isListEven = numbers.stream().allMatch(n -> n % 2 == 0);
        System.out.println(isListEven);

        // Kiểm tra xem list có phần tử  > 10 hay không
        boolean listHasElementGreater10 = numbers.stream().anyMatch(n -> n > 10);
        System.out.println(listHasElementGreater10);
        System.out.println();

        // Có bao nhiêu phần tử  > 5
        long countElementGreater5 = numbers.stream().filter(n -> n > 5).count();
        System.out.println(countElementGreater5);
        System.out.println();

        // Nhân đôi các phần tủ trong list và trả về list mới
        List<Integer> numbersX2 = numbers.stream().map(n -> n * 2).toList();
        numbersX2.stream().forEach(System.out::println);
        System.out.println();
    }

}
