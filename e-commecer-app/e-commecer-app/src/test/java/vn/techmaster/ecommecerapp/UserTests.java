package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.techmaster.ecommecerapp.entity.Role;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.repository.RoleRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;
import vn.techmaster.ecommecerapp.utils.StringUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
        Date start = new Calendar.Builder().setDate(2023, 1, 1).build().getTime();
        Date end = new Date();
        for (int i = 0; i < 50; i++) {
            User user = new User();
            String name = faker.name().username();
            user.setUsername(name);
            user.setEmail(faker.internet().emailAddress());
            user.setPhone(faker.phoneNumber().phoneNumber());
            user.setAvatar(generateLinkAuthorAvatar(name));
            user.setEnabled(true);
            user.setCreatedAt(randomDateBetweenTwoDates(start, end));
            user.setRoles(Set.of(roleRepository.findByName("USER").get()));
            user.setPassword(passwordEncoder.encode("123"));
            userRepository.save(user);
        }
    }

    @Test
    void update_avatar_for_user() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            String name = user.getUsername();
            user.setAvatar(generateLinkAuthorAvatar(name));
            userRepository.save(user);
        });
    }

    // get character first each of word from string, and to uppercase
    private String getCharacter(String str) {
        String[] words = str.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(word.charAt(0));
        }
        return result.toString().toUpperCase();
    }

    // generate link author avatar follow struct : https://placehold.co/200x200?text=[...]
    private String generateLinkAuthorAvatar(String authorName) {
        return "https://placehold.co/200x200?text=" + getCharacter(authorName);
    }

    // write method to random date between 2 date
    private Date randomDateBetweenTwoDates(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
          .current()
          .nextLong(startMillis, endMillis);
        return new Date(randomMillisSinceEpoch);
    }

    @Test
    void update_created_at_for_user() {
        List<User> users = userRepository.findAll();
        // start date 2023-01-01 not using new Date
        Date start = new Calendar.Builder().setDate(2023, 8, 1).build().getTime();
        Date end = new Date();

        for (User user : users) {
            user.setCreatedAt(randomDateBetweenTwoDates(start, end));
            userRepository.save(user);
        }
    }

    @Test
    void random_user_disabled() {
        Random random = new Random();
        List<User> users = userRepository.findAll();

        List<User> rdUsers = new ArrayList<>();

        // random 10 user unique using for loop and add to list rdUsers
        for (int i = 0; i < 15; i++) {
            int rd = random.nextInt(users.size());
            if (!rdUsers.contains(users.get(rd))) {
                rdUsers.add(users.get(rd));
            }
        }

        // set enabled = false for user in list rdUsers
        for (User user : rdUsers) {
            user.setEnabled(false);
            userRepository.save(user);
        }
    }

    @Test
    void update_info_user() {
        // Random ds họ người dùng theo tên gọi Việt Nam
        Random random = new Random();

        // Tạo slugify object không có dấu -
        Slugify slugify = Slugify.builder().build();

        List<String> listHo = List.of(
                "Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ", "Võ", "Đặng",
                "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý", "Đào", "Đinh", "Lâm", "Phùng", "Mai",
                "Tô", "Trịnh", "Đoàn", "Tăng", "Bành", "Hà", "Thái", "Tạ", "Tăng", "Thi"
        );

        // Random ds tên đệm người dùng theo tên gọi Việt Nam
        List<String> listTenDem = List.of(
                "Văn", "Thị", "Hồng", "Hải", "Hà", "Hưng", "Hùng", "Hạnh", "Hạ", "Thanh");

        // Random ds tên người dùng theo tên gọi Việt Nam (30 tên phổ biến từ A -> Z) (ít vần H)
        List<String> listTen = List.of(
                "An", "Bình", "Cường", "Dũng", "Đức", "Giang", "Hải", "Hào", "Hùng", "Hưng", "Minh", "Nam", "Nghĩa", "Phong", "Phúc", "Quân", "Quang", "Quốc", "Sơn", "Thắng", "Thành", "Thiên", "Thịnh", "Thuận", "Tiến", "Trung", "Tuấn", "Vinh", "Vũ", "Xuân"
        );

        // Random ds số điện thoại Việt Nam bao gồm 10 số
        List<String> phones = List.of(
                "086", "096", "097", "098", "032", "033", "034", "035", "036", "037", "038", "039",
                "090", "093", "070", "079", "077", "076", "078", "089", "088", "091", "094", "083",
                "085", "081", "082", "092", "056", "058", "099"
        );

        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            String ho = listHo.get(random.nextInt(listHo.size()));
            String tenDem = listTenDem.get(random.nextInt(listTenDem.size()));
            String ten = listTen.get(random.nextInt(listTen.size()));

            String fullName = ho + " " + tenDem + " " + ten;
            String email = slugify.slugify(fullName.toLowerCase()).replaceAll("-", "") + "@gmail.com";

            user.setUsername(ho + " " + tenDem + " " + ten);
            user.setAvatar(StringUtils.generateLinkImage(ten));
            user.setEmail(email);

            StringBuilder phone = new StringBuilder(phones.get(random.nextInt(phones.size())));
            for (int i = 0; i < 7; i++) {
                phone.append(random.nextInt(10));
            }
            user.setPhone(phone.toString());

            userRepository.save(user);
        }
    }

}
