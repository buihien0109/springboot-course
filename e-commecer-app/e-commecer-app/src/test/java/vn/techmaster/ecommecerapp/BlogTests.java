package vn.techmaster.ecommecerapp;

import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.ecommecerapp.entity.Blog;
import vn.techmaster.ecommecerapp.entity.Tag;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.model.dto.BlogDto;
import vn.techmaster.ecommecerapp.repository.BlogRepository;
import vn.techmaster.ecommecerapp.repository.TagRepository;
import vn.techmaster.ecommecerapp.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
public class BlogTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private BlogRepository blogRepository;

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

        for (int i = 0; i < 10; i++) {
            // Random 1 user
            User rdUser = userList.get(rd.nextInt(userList.size()));

            // Random 1 ds category tuong ung
            List<Tag> rdList = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                Tag rdTag = tagList.get(rd.nextInt(tagList.size()));
                if (!rdList.contains(rdTag)) {
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
    void update_tag_slug() {
        Slugify slugify = Slugify.builder().build();
        List<Tag> tagList = tagRepository.findAll();
        for (Tag tag : tagList) {
            tag.setSlug(slugify.slugify(tag.getName()));
        }
        tagRepository.saveAll(tagList);
    }

    @Test
    void update_info_blog() {
        Blog blogSample = blogRepository.findById(25).get();
        List<Blog> blogList = blogRepository.findAll();

        Date start = new Calendar.Builder().setDate(2023, 8, 1).build().getTime();
        Date end = new Date();
        for (Blog blog : blogList) {
            Date rdDate = randomDateBetweenTwoDates(start, end);
            if (!List.of(24, 25, 27).contains(blog.getId())) {
                blog.setContent(blogSample.getContent());
                blog.setDescription(blogSample.getDescription());
                blog.setThumbnail(generateLinkImage(blog.getTitle()));
            }
            blog.setCreatedAt(rdDate);
            blog.setUpdatedAt(rdDate);
            blog.setPublishedAt(rdDate);
            blog.setStatus(true);
            blogRepository.save(blog);
        }
    }

    @Test
    void update_blog_date() {
        List<Blog> blogList = blogRepository.findAll();

        Date start = new Calendar.Builder().setDate(2023, 8, 1).build().getTime();
        Date end = new Date();

        for (Blog blog : blogList) {
            Date rdDate = randomDateBetweenTwoDates(start, end);
            blog.setCreatedAt(rdDate);
            blog.setUpdatedAt(rdDate);
            blog.setPublishedAt(rdDate);
            blog.setStatus(true);
            blogRepository.save(blog);
        }
    }

    @Test
    void update_blog_category() {
        Random rd = new Random();
        List<Blog> blogList = blogRepository.findAll();
        List<Tag> tagList = tagRepository.findAll();

        for (Blog blog : blogList) {
            // Random 1 ds category tuong ung
            List<Tag> rdList = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                Tag rdTag = tagList.get(rd.nextInt(tagList.size()));
                if (!rdList.contains(rdTag)) {
                    rdList.add(rdTag);
                }
            }

            blog.setTags(rdList);
            blogRepository.save(blog);
        }
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

    // get character first each of word from string, and to uppercase
    private String getCharacter(String str) {
        String[] words = str.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(word.charAt(0));
        }
        if (result.length() >= 2) {
            return result.substring(0, 2).toUpperCase();
        } else {
            return result.toString().toUpperCase();
        }
    }

    // generate link author avatar follow struct : https://placehold.co/200x200?text=[...]
    private String generateLinkImage(String authorName) {
        return "https://placehold.co/200x200?text=" + getCharacter(authorName);
    }

    @Test
    void findAllBlogDtos() {
        List<Blog> blogList = blogRepository.findAllBlogs();
    }
}
