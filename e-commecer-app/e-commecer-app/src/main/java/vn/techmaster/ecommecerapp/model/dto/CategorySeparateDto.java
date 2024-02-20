package vn.techmaster.ecommecerapp.model.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategorySeparateDto {
    CategoryDto mainCategory;
    List<CategoryDto> subCategories;

    public CategorySeparateDto(String mainCategory, String subCategories) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.mainCategory = objectMapper.readValue((String) mainCategory, CategoryDto.class);
        } catch (IOException e) {
            this.mainCategory = null;
        }

        try {
            this.subCategories = objectMapper.readValue((String) subCategories, new TypeReference<List<CategoryDto>>() {
            });
        } catch (IOException e) {
            this.subCategories = new ArrayList<>();
        }
    }
}
