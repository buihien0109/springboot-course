package vn.techmaster.ecommecerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.techmaster.ecommecerapp.entity.Tag;
import vn.techmaster.ecommecerapp.model.dto.TagDto;
import vn.techmaster.ecommecerapp.model.dto.TagUsedDto;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findByIdIn(List<Integer> tagIds);

    Optional<Tag> findByName(String name);

    @Query("SELECT new vn.techmaster.ecommecerapp.model.dto.TagDto(t.id, t.name, t.slug) FROM Tag t")
    List<TagDto> getAllTags();

    @Query(nativeQuery = true, name = "getAllTagsUsedDto")
    List<TagUsedDto> getAllTagsUsedDto();
}