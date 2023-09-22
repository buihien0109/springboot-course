package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.*;
import vn.techmaster.ecommecerapp.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class BlogTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void save_categories() {
        List<Tag> tags = List.of(
                new Tag(null, "Làm đẹp"),
                new Tag(null, "Sức khoẻ"),
                new Tag(null, "Cuộc sống"),
                new Tag(null, "Trái cây"),
                new Tag(null, "Đồ ăn"),
                new Tag(null, "Khuyến mại")
        );
        tagRepository.saveAll(tags);
    }

    @Test
    void save_blogs() {
        Faker faker = new Faker();
        Slugify slugify = Slugify.builder().build();
        Random rd = new Random();

        List<User> userList = userRepository.findByRoles_NameIn(List.of("ADMIN"));
        List<Tag> tagList = tagRepository.findAll();

        for (int i = 0; i < 20; i++) {
            // Random 1 user
            User rdUser = userList.get(rd.nextInt(userList.size()));

            // Random 1 ds category tuong ung
            List<Tag> rdList = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                Tag rdTag = tagList.get(rd.nextInt(tagList.size()));
                if(!rdList.contains(rdTag)) {
                    rdList.add(rdTag);
                }
            }

            // Tao blog
            String title = faker.book().title();
            Blog blog = Blog.builder()
                    .title(title)
                    .slug(slugify.slugify(title))
                    .description(faker.lorem().paragraph())
                    .content(faker.lorem().paragraph(20))
                    .status(rd.nextInt(2) == 0)
                    .user(rdUser)
                    .tags(rdList)
                    .build();

            blogRepository.save(blog);
        }
    }

    @Test
    void save_comments() {
        Faker faker = new Faker();
        Random rd = new Random();
        List<User> userList = userRepository.findAll();
        List<Blog> blogList = blogRepository.findAll();

        for (int i = 0; i < 100; i++) {
            // Random 1 user
            User rdUser = userList.get(rd.nextInt(userList.size()));

            // Random 1 blog
            Blog rdBlog = blogList.get(rd.nextInt(blogList.size()));

            // Tao comment
            Comment comment = Comment.builder()
                    .content(faker.lorem().paragraph())
                    .user(rdUser)
                    .blog(rdBlog)
                    .build();

            commentRepository.save(comment);
        }
    }

    @Test
    void update_tag_slug() {
        Slugify slugify = Slugify.builder().build();
        List<Tag> tagList = tagRepository.findAll();
        for (Tag tag : tagList) {
            tag.setSlug(slugify.slugify(tag.getName()));
        }
        tagRepository.saveAll(tagList);
    }
}
