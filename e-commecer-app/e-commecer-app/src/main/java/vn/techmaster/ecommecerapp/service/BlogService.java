package vn.techmaster.ecommecerapp.service;

import com.github.slugify.Slugify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Blog;
import vn.techmaster.ecommecerapp.entity.Tag;
import vn.techmaster.ecommecerapp.entity.User;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.BlogPublic;
import vn.techmaster.ecommecerapp.model.request.UpsertBlogRequest;
import vn.techmaster.ecommecerapp.repository.BlogRepository;
import vn.techmaster.ecommecerapp.repository.TagRepository;
import vn.techmaster.ecommecerapp.security.SecurityUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogService {
    private final BlogRepository blogRepository;
    private final TagRepository tagRepository;

    public BlogService(BlogRepository blogRepository, TagRepository tagRepository) {
        this.blogRepository = blogRepository;
        this.tagRepository = tagRepository;
    }

    // get all blogs have status = 1 and sort by published_at desc and pagination by page and limit
    public Page<BlogPublic> getAllBlogs(String tagSlug, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("publishedAt").descending());
        Page<Blog> pageData;
        if (tagSlug == null || tagSlug.isEmpty()) {
            pageData = blogRepository.findByStatus(true, pageable);
        } else {
            pageData = blogRepository.findByStatusAndTags_Slug(true, tagSlug, pageable);
        }
        return pageData.map(BlogPublic::of);
    }

    public List<BlogPublic> getAllBlogs() {
        List<Blog> blogs = blogRepository.findByStatus(true, Sort.by("publishedAt"));
        return blogs.stream().map(BlogPublic::of).toList();
    }

    public List<BlogPublic> getAllBlogs(Integer limit) {
        return getAllBlogs().stream().limit(limit).toList();
    }

    public Page<BlogPublic> getAllBlogs(Integer page, Integer limit) {
        Page<Blog> pageData = blogRepository.findAll(PageRequest.of(page - 1, limit, Sort.by("createdAt").descending()));
        return pageData.map(BlogPublic::of);
    }

    public Page<BlogPublic> getAllOwnBlog(Integer page, Integer pageSize) {
        User user = SecurityUtils.getCurrentUserLogin();
        Page<Blog> pageData = blogRepository.findByUser_UserIdOrderByCreatedAtDesc(
                user.getUserId(),
                PageRequest.of(page - 1, pageSize)
        );

        return pageData.map(BlogPublic::of);
    }

    // get blog by id and slug
    public BlogPublic getBlogByIdAndSlug(Integer id, String slug) {
        Blog blog = blogRepository.findByIdAndSlugAndStatus(id, slug, true)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết với id = " + id + " và slug = " + slug));
        return BlogPublic.of(blog);
    }

    // Get all posts related to main post by id, slug
    public List<BlogPublic> getRelatedBlogs(Integer id, String slug) {
        Blog blog = blogRepository.findByIdAndSlugAndStatus(id, slug, true)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết với id = " + id + " và slug = " + slug));
        return blog.getTags().stream()
                .flatMap(tag -> tag.getBlogs().stream())
                .filter(b -> b.getStatus() && !b.getId().equals(id))
                .distinct()
                .limit(3)
                .map(BlogPublic::of)
                .toList();
    }

    public BlogPublic getBlogById(Integer id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy bài viết với id = " + id));
        return BlogPublic.of(blog);
    }

    public BlogPublic createBlog(UpsertBlogRequest request) {
        User user = SecurityUtils.getCurrentUserLogin();
        List<Tag> tags = tagRepository.findByIdIn(request.getTagIds());

        // Create blog
        Slugify slugify = Slugify.builder().build();
        Blog blog = Blog.builder()
                .title(request.getTitle())
                .slug(slugify.slugify(request.getTitle()))
                .content(request.getContent())
                .description(request.getDescription())
                .thumbnail(request.getThumbnail())
                .status(request.getStatus())
                .tags(tags)
                .comments(new ArrayList<>())
                .user(user)
                .build();

        blogRepository.save(blog);
        return BlogPublic.of(blog);
    }

    public BlogPublic updateBlogById(Integer id, UpsertBlogRequest request) {
        // find blog by id
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy bài viết với id = " + id));

        // find tags by tagIds
        List<Tag> tags = tagRepository.findByIdIn(request.getTagIds());

        // update blog
        Slugify slugify = Slugify.builder().build();
        blog.setTitle(request.getTitle());
        blog.setSlug(slugify.slugify(request.getTitle()));
        blog.setDescription(request.getDescription());
        blog.setContent(request.getContent());
        blog.setStatus(request.getStatus());
        blog.setThumbnail(request.getThumbnail());
        blog.setTags(tags);

        blogRepository.save(blog);
        return BlogPublic.of(blog);
    }

    public void deleteBlogById(Integer id) {
        // find blog by id
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy bài viết với id = " + id));

        blogRepository.delete(blog);
    }
}
