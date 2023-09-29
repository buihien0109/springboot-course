package vn.techmaster.ecommecerapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Tag;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.projection.TagPublic;
import vn.techmaster.ecommecerapp.model.request.UpsertTagRequest;
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

    public Page<TagPublic> getAllTags(Integer page, Integer limit) {
        Page<Tag> pageData = tagRepository.findAll(PageRequest.of(page - 1, limit));
        return pageData.map(TagPublic::of);
    }

    public TagPublic createTag(UpsertTagRequest request) {
        // check tag name is exist
        if (tagRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Tag đã tồn tại");
        }

        Tag tag = new Tag();
        tag.setName(request.getName());
        tagRepository.save(tag);
        return TagPublic.of(tag);
    }

    public TagPublic updateTagById(Integer id, UpsertTagRequest request) {
        // find tag by id
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy tag có id = " + id));

        if (tagRepository.findByName(request.getName()).isPresent()
                && !tagRepository.findByName(request.getName()).get().getName().equals(tag.getName())) {
            throw new BadRequestException("Tag đã tồn tại");
        }

        tag.setName(request.getName());
        tagRepository.save(tag);
        return TagPublic.of(tag);
    }

    public void deleteTagById(Integer id) {
        // find tag by id
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy tag có id = " + id));

        // if tag used by blog => throw exception
        if (!tag.getBlogs().isEmpty()) {
            throw new BadRequestException("Không thể xóa tag đang được sử dụng");
        }

        tagRepository.delete(tag);
    }
}
