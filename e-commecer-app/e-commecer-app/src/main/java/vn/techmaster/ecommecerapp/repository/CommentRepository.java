package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}