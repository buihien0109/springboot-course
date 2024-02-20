package vn.techmaster.ecommecerapp.service;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.Tag;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.exception.ResouceNotFoundException;
import vn.techmaster.ecommecerapp.model.dto.TagDto;
import vn.techmaster.ecommecerapp.model.dto.TagUsedDto;
import vn.techmaster.ecommecerapp.model.projection.BlogPublic;
import vn.techmaster.ecommecerapp.model.projection.TagAdminPublic;
import vn.techmaster.ecommecerapp.model.projection.TagPublic;
import vn.techmaster.ecommecerapp.model.request.UpsertTagRequest;
import vn.techmaster.ecommecerapp.repository.TagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final Slugify slugify;

    public List<TagDto> getAllTags() {
        return tagRepository.getAllTags();
    }

    public Page<TagPublic> getAllTags(Integer page, Integer limit) {
        Page<Tag> pageData = tagRepository.findAll(PageRequest.of(page - 1, limit));
        return pageData.map(TagPublic::of);
    }

    public List<TagUsedDto> getAllTagsAdmin() {
        return tagRepository.getAllTagsUsedDto();
    }

    public TagAdminPublic createTag(UpsertTagRequest request) {
        // check tag name is exist
        if (tagRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Tag đã tồn tại");
        }

        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setSlug(slugify.slugify(request.getName()));
        tagRepository.save(tag);
        return TagAdminPublic.of(tag);
    }

    public TagAdminPublic updateTagById(Integer id, UpsertTagRequest request) {
        // find tag by id
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy tag có id = " + id));

        if (tagRepository.findByName(request.getName()).isPresent()
                && !tagRepository.findByName(request.getName()).get().getName().equals(tag.getName())) {
            throw new BadRequestException("Tag đã tồn tại");
        }

        tag.setName(request.getName());
        tag.setSlug(slugify.slugify(request.getName()));
        tagRepository.save(tag);
        return TagAdminPublic.of(tag);
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

    public List<BlogPublic> getAllBlogsByTagId(Integer id) {
        // find tag by id
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Không tìm thấy tag có id = " + id));

        return tag.getBlogs().stream().map(BlogPublic::of).toList();
    }
}
