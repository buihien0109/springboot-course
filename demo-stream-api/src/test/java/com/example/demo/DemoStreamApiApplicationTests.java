package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class DemoStreamApiApplicationTests {
    List<Person> people = new ArrayList<>(List.of(
            new Person(1, "Nguyễn Văn A", "Engineer", "Male", "Hà Nội", 50000, LocalDate.of(1990, 1, 1)),
            new Person(2, "Trần Thị B", "Teacher", "Female", "TP. Hồ Chí Minh", 45000, LocalDate.of(1985, 2, 15)),
            new Person(3, "Lê Văn C", "Doctor", "Male", "Đà Nẵng", 52000, LocalDate.of(1992, 3, 21)),
            new Person(4, "Phạm Thị D", "Nurse", "Female", "Cần Thơ", 43000, LocalDate.of(1988, 4, 10)),
            new Person(5, "Võ Văn E", "Artist", "Male", "Hải Phòng", 48000, LocalDate.of(1991, 5, 5)),
            new Person(6, "Hoàng Thị F", "Chef", "Female", "Nha Trang", 46000, LocalDate.of(1987, 6, 16)),
            new Person(7, "Bùi Văn G", "Lawyer", "Male", "Hà Nội", 55000, LocalDate.of(1989, 7, 24)),
            new Person(8, "Đặng Thị H", "Architect", "Female", "TP. Hồ Chí Minh", 47000, LocalDate.of(1993, 8, 8)),
            new Person(9, "Ngô Văn I", "Programmer", "Male", "Đà Nẵng", 51000, LocalDate.of(1986, 9, 9)),
            new Person(10, "Đinh Thị J", "Journalist", "Female", "Cần Thơ", 44000, LocalDate.of(1990, 10, 15))
    ));

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static
    class Person {
        private Integer id;
        private String fullname;
        private String job;
        private String gender;
        private String city;
        private Integer salary;
        private LocalDate birthday;
    }

    @Test
    void example_test() {
        List<Map.Entry<String, Double>> rs = top5HighestSalaryCities();
        rs.forEach(System.out::println);
    }



    public List<Map.Entry<String, Double>> top5HighestSalaryCities() {
        people.stream()
                .collect(Collectors.groupingBy(Person::getJob, Collectors.summingInt(e -> 1)));
        return people.stream()
                .collect(Collectors.groupingBy(Person::getCity,
                        Collectors.averagingDouble(Person::getSalary)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

}
