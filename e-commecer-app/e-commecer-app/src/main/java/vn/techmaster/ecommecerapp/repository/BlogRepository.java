package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.techmaster.ecommecerapp.entity.Blog;
import vn.techmaster.ecommecerapp.model.dto.BlogDto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
    @Query("SELECT DISTINCT b FROM Blog b " +
            "LEFT JOIN FETCH b.user " +
            "LEFT JOIN FETCH b.tags")
    List<Blog> findAllBlogs();

    Page<Blog> findByStatus(Boolean status, Pageable pageable);

    List<Blog> findByStatus(Boolean status, Sort sort);

    Page<Blog> findByStatusAndTags_Slug(Boolean status, String slug, Pageable pageable);

    Optional<Blog> findByIdAndSlugAndStatus(Integer id, String slug, Boolean status);

    Page<Blog> findByUser_UserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<Blog> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

    long countByCreatedAtBetween(Date start, Date end);
}