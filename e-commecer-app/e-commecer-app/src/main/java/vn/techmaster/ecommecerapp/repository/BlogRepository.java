package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.techmaster.ecommecerapp.entity.Blog;
import vn.techmaster.ecommecerapp.model.dto.BlogDto;
import vn.techmaster.ecommecerapp.model.dto.BlogWebDto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
    @Query(nativeQuery = true, name = "getAllBlogAdmin")
    List<BlogDto> getAllBlogAdmin();

    @Query(nativeQuery = true, name = "getAllOwnBlogAdmin")
    List<BlogDto> getAllOwnBlogAdmin(Long userId);

    @Query(nativeQuery = true, name = "getBlogDtoById")
    Optional<BlogDto> getBlogDtoById(Integer blogId);

    List<Blog> findByStatus(Boolean status, Sort sort);

    Optional<Blog> findByIdAndSlugAndStatus(Integer id, String slug, Boolean status);

    Page<Blog> findByUser_UserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<Blog> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

    long countByCreatedAtBetween(Date start, Date end);

    // find all blogs by status = 1 and tag_slug and title containing ignorecase keyword and sort by published_at desc and pagination by page and limit
    // but tag_slug and keyword are optional (nullable)
    @Query("SELECT DISTINCT new vn.techmaster.ecommecerapp.model.dto.BlogWebDto(b.id, b.title, b.slug, b.description, b.thumbnail, b.publishedAt) FROM Blog b " +
            "LEFT JOIN b.tags t " +
            "WHERE (:tagSlug IS NULL OR :tagSlug = '' OR t.slug = :tagSlug) " +
            "AND (:keyword IS NULL OR :keyword = '' OR LOWER(b.title) LIKE %:keyword%) " +
            "AND b.status = :status")
    Page<BlogWebDto> searchBlog(Boolean status, String tagSlug, String keyword, Pageable pageable);


    @Query("SELECT DISTINCT new vn.techmaster.ecommecerapp.model.dto.BlogWebDto(b.id, b.title, b.slug, b.description, b.thumbnail, b.publishedAt) FROM Blog b " +
            "WHERE b.status = :status")
    Page<BlogWebDto> findLatestBlog(Boolean status, Pageable pageable);

    // find all blogs by status = 1 and sort by published_at desc and pagination by page and limit
    // recommend blogs are blogs that have the same tag with the current blog
    @Query("SELECT DISTINCT new vn.techmaster.ecommecerapp.model.dto.BlogWebDto(b.id, b.title, b.slug, b.description, b.thumbnail, b.publishedAt) FROM Blog b " +
            "LEFT JOIN b.tags t " +
            "WHERE b.id <> ?1 " +
            "AND b.status = ?2 " +
            "AND t IN (SELECT t FROM Blog b JOIN b.tags t WHERE b.id = ?1)")
    Page<BlogWebDto> getRecommendBlogs(Integer blogId, Boolean status, Pageable pageable);

    // find blog by id and slug and status -> return BlogDto using native query
    @Query(name = "findBlogDtoByIdAndSlugAndStatus", nativeQuery = true)
    Optional<BlogDto> findBlogDtoByIdAndSlugAndStatus(Integer id, String slug, Boolean status);
}