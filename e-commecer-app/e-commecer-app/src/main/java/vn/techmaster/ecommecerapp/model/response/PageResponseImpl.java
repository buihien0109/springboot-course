package vn.techmaster.ecommecerapp.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponseImpl<T> implements PageResponse<T> {
    @JsonIgnore
    List<T> data;

    @JsonIgnore
    Integer currentPage;

    @JsonIgnore
    Integer limit;

    @Override
    public Integer getCurrentPage() {
        return currentPage;
    }

    @Override
    public Integer getLimit() {
        return limit;
    }

    @Override
    public Integer getTotalElements() {
        return data.size();
    }

    @Override
    public Integer getTotalPages() {
        return (int) Math.ceil((double) data.size() / limit);
    }

    @Override
    public List<T> getContent() {
        int fromIndex = (currentPage - 1) * limit;
        int toIndex = Math.min(fromIndex + limit, data.size());
        return data.subList(fromIndex, toIndex);
    }

    @Override
    public Integer getPreviousPage() {
        return currentPage - 1;
    }

    @Override
    public Integer getNextPage() {
        return currentPage + 1;
    }

    @Override
    public Boolean hasPreviousPage() {
        return currentPage > 1;
    }

    @Override
    public Boolean hasNextPage() {
        return currentPage < getTotalPages();
    }

    @Override
    public Boolean isFirstPage() {
        return currentPage == 1;
    }

    @Override
    public Boolean isLastPage() {
        return currentPage.equals(getTotalPages());
    }
}
