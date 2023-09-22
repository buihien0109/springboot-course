package vn.techmaster.ecommecerapp.service;

import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.model.projection.TagPublic;
import vn.techmaster.ecommecerapp.repository.TagRepository;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagPublic> getAllTags() {
        return tagRepository.findAll().stream().map(TagPublic::of).toList();
    }
}
