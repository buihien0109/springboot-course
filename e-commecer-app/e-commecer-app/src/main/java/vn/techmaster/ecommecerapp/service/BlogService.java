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
        List<Blog> blogs = blogRepository.findByStatus(true, Sort.by("publishedAt"));;
        return blogs.stream().map(BlogPublic::of).toList();
    }

    public List<BlogPublic> getAllBlogs(Integer limit) {
        return getAllBlogs().stream().limit(limit).toList();
    }
}
