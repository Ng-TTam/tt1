package com.manage.userManagement.mapper;

import com.manage.userManagement.dto.request.CategoryRequest;
import com.manage.userManagement.dto.response.CategoryResponse;
import com.manage.userManagement.entity.Category;
import com.manage.userManagement.entity.CategoryDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest categoryRequest);

    CategoryResponse toCategoryResponse(Category category);

    CategoryDocument toCategoryDocument(Category category);

    void updateCategory(@MappingTarget Category category, CategoryRequest categoryRequest);
}
