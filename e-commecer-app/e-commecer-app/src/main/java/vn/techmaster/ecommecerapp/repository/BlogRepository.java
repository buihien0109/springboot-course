package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Blog;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
    Page<Blog> findByStatus(Boolean status, Pageable pageable);

    List<Blog> findByStatus(Boolean status, Sort sort);

    Page<Blog> findByStatusAndTags_Slug(Boolean status, String slug, Pageable pageable);

    Optional<Blog> findByIdAndSlugAndStatus(Integer id, String slug, Boolean status);

    Page<Blog> findByUser_UserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<Blog> findByUser_UserIdOrderByCreatedAtDesc(Long userId);
}