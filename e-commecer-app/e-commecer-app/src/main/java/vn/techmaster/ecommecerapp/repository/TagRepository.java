package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.ecommecerapp.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer> {
}