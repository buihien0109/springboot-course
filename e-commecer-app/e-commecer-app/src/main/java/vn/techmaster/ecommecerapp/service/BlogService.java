package vn.techmaster.ecommecerapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Blog;
import vn.techmaster.ecommecerapp.model.projection.BlogPublic;
import vn.techmaster.ecommecerapp.repository.BlogRepository;

import java.util.List;

@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
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

    // get blog by id and slug
    public BlogPublic getBlogByIdAndSlug(Integer id, String slug) {
        Blog blog = blogRepository.findByIdAndSlugAndStatus(id, slug, true)
                .orElseThrow(() -> new RuntimeException("Cannot find blog with id = " + id + " and slug = " + slug));
        return BlogPublic.of(blog);
    }

    // Get all posts related to main post by id, slug
    public List<BlogPublic> getRelatedBlogs(Integer id, String slug) {
        Blog blog = blogRepository.findByIdAndSlugAndStatus(id, slug, true)
                .orElseThrow(() -> new RuntimeException("Cannot find blog with id = " + id + " and slug = " + slug));
        return blog.getTags().stream()
                .flatMap(tag -> tag.getBlogs().stream())
                .filter(b -> b.getStatus() && !b.getId().equals(id))
                .distinct()
                .limit(3)
                .map(BlogPublic::of)
                .toList();
    }
}
