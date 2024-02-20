package vn.techmaster.demoauthsessioncookie;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import vn.techmaster.demoauthsessioncookie.entity.Movie;
import vn.techmaster.demoauthsessioncookie.entity.Review;
import vn.techmaster.demoauthsessioncookie.entity.User;
import vn.techmaster.demoauthsessioncookie.model.enums.UserRole;
import vn.techmaster.demoauthsessioncookie.repository.MovieRepository;
import vn.techmaster.demoauthsessioncookie.repository.ReviewRepository;
import vn.techmaster.demoauthsessioncookie.repository.UserRepository;

import java.util.List;
import java.util.Random;

@SpringBootTest
class DemoAuthSessionCookieApplicationTests {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void save_all_movie() {
        Faker faker = new Faker(); // Faker data

        for (int i = 0; i < 20; i++) {
            Movie movie = Movie.builder()
                    .title(faker.book().title())
                    .build();
            movieRepository.save(movie); // Lưu vào database
        }
    }

    @Test
    void save_all_user() {
        Faker faker = new Faker(); // Faker data
        for (int i = 0; i < 10; i++) {
            String name = faker.name().fullName();
            User user = User.builder()
                    .name(name)
                    .email(faker.internet().emailAddress())
                    .password(bCryptPasswordEncoder.encode("123"))
                    .role(i == 0 || i == 1 ? UserRole.ADMIN : UserRole.USER)
                    .build();

            userRepository.save(user); // Lưu vào database
        }
    }

    @Test
    void save_all_review() {
        List<User> userList = userRepository.findAll();
        List<Movie> movieList = movieRepository.findAll();

        Faker faker = new Faker(); // Faker data
        Random random = new Random();

        for (Movie movie : movieList) {
            // Mỗi movie có 5 -> 10 review
            for (int i = 0; i < random.nextInt(5) + 5; i++) {
                // Random 1 user
                User rdUser = userList.get(random.nextInt(userList.size()));

                // Tạo review
                Review review = Review.builder()
                        .content(faker.lorem().paragraph())
                        .movie(movie)
                        .user(rdUser)
                        .build();

                // Lưu vào database
                reviewRepository.save(review);
            }
        }
    }

    // generate link author avatar follow struct : https://placehold.co/200x200?text=[...]
    public static String generateLinkImage(String str) {
        return "https://placehold.co/200x200?text=" + str.substring(0, 1).toUpperCase();
    }

}
