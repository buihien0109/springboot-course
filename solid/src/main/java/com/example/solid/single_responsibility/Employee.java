package com.example.solid.single_responsibility;

// Single Responsibility principle
// Mỗi class chỉ nên chịu trách nhiệm về một nhiệm vụ cụ thể nào đó mà thôi.
// Không có nhiều hơn một lý do để chỉnh sửa một class.
public class Employee {
    public void work() {
        System.out.println("Làm việc");
    }
    public void calculateSalary() {
        System.out.println("Tính lương");
    }
    public void connectDatabase() {
        System.out.println("Kết nối cơ sở dữ liệu");
    }
}
