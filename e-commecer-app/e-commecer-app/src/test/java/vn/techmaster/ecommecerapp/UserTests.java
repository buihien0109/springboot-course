package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.techmaster.ecommecerapp.entity.Role;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.repository.RoleRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class UserTests {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    void save_roles() {
        Role roleAdmin = new Role("ADMIN");
        Role roleUser = new Role("USER");

        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);
    }

    @Test
    void save_users() {
        Role roleAdmin = roleRepository.findByName("ADMIN").get();
        Role roleUser = roleRepository.findByName("USER").get();

        User user1 = new User("admin", "admin@gmail.com", "123456", Set.of(roleAdmin, roleUser));
        User user2 = new User("user", "user@gmail.com", "123456", Set.of(roleUser));
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    void change_password() {
        User user = userRepository.findById(1L).get();
        user.setPassword(passwordEncoder.encode("123"));
        userRepository.save(user);

        User user1 = userRepository.findById(2L).get();
        user1.setPassword(passwordEncoder.encode("123"));
        userRepository.save(user1);
    }

    @Test
    void save_users_1() {
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setUsername(faker.name().username());
            user.setEmail(faker.internet().emailAddress());
            user.setPhone(faker.phoneNumber().phoneNumber());
            user.setAvatar(faker.company().logo());
            user.setRoles(Set.of(roleRepository.findByName("USER").get()));
            user.setPassword(passwordEncoder.encode("123"));
            userRepository.save(user);
        }
    }
}
