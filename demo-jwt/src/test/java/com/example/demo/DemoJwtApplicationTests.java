package com.example.demo;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class DemoJwtApplicationTests {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void save_role() {
        Role roleAdmin = new Role();
        roleAdmin.setName("ADMIN");
        roleRepository.save(roleAdmin);

        Role roleUser = new Role();
        roleUser.setName("USER");
        roleRepository.save(roleUser);
    }

    @Test
    void save_user() {
        // get role by name
        Role roleAdmin = roleRepository.findByName("ADMIN").get();
        Role roleUser = roleRepository.findByName("USER").get();

        // create user
        User userAdmin = new User();
        userAdmin.setName("Bùi Hiên");
        userAdmin.setEmail("hien@gmail.com");
        userAdmin.setPassword(passwordEncoder.encode("123"));
        userAdmin.getRoles().add(roleAdmin);
        userAdmin.getRoles().add(roleUser);

        User userUser = new User();
        userUser.setName("Minh Duy");
        userUser.setEmail("duy@gmail.com");
        userUser.setPassword(passwordEncoder.encode("123"));
        userUser.getRoles().add(roleUser);

        // save user
        userRepository.save(userAdmin);
        userRepository.save(userUser);
    }
}
