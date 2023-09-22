package vn.techmaster.ecommecerapp.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PagePublic<T> {
    List<T> getContent();

    int getNumber();

    int getSize();

    int getTotalPages();

    int getTotalElements();

    boolean isFirst();

    boolean isLast();
    @RequiredArgsConstructor
    class PagePublicImpl<T> implements PagePublic<T> {
        @JsonIgnore
        private final Page<T> page;

        @Override
        public List<T> getContent() {
            return page.getContent();
        }

        @Override
        public int getNumber() {
            return page.getNumber();
        }

        @Override
        public int getSize() {
            return page.getSize();
        }

        @Override
        public int getTotalPages() {
            return page.getTotalPages();
        }

        @Override
        public int getTotalElements() {
            return (int) page.getTotalElements();
        }

        @Override
        public boolean isFirst() {
            return page.isFirst();
        }

        @Override
        public boolean isLast() {
            return page.isLast();
        }
    }

    static <T> PagePublic<T> of(Page<T> page) {
        return new PagePublicImpl<>(page);
    }
}
