package com.example.demo;

import com.example.demo.entity.Role;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
class DemoJpaAuditingApplicationTests {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    void save_student() {
        Student student = new Student("Bùi Hiên");
        studentRepository.save(student);
    }

    @Test
    void save_mutilple_student() {
        for (int i = 0; i < 10; i++) {
            try {
                Student student = new Student();
                studentRepository.save(student);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Test
    void get_by_id() {
        Student student = studentRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Not found"));
        System.out.println(student);
    }

    @Test
    void update_name_student() {
        Student student = studentRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Not found"));
        student.setName("Thu Hằng");
        studentRepository.save(student);
    }

    @Test
    void save_roles() {
        Role roleUser = new Role(null, "USER");
        Role roleAdmin = new Role(null, "ADMIN");
        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);
    }

    @Test
    void save_users() {
        Role roleUser = roleRepository.findByName("USER").orElse(null);
        Role roleAdmin = roleRepository.findByName("ADMIN").orElse(null);
        User user = new User(null, "Bùi Hiên", "hien@gmail.com", passwordEncoder.encode("111"), List.of(roleUser, roleAdmin));
        User user1 = new User(null, "Thu Hằng", "hang@gmail.com", passwordEncoder.encode("111"), List.of(roleUser));

        userRepository.save(user);
        userRepository.save(user1);
    }
}
