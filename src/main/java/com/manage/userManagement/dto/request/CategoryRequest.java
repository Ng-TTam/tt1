package com.manage.userManagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {

    @NotBlank(message = "BLANK_CATEGORY_NAME")
    String name;

    @NotBlank(message = "BLANK_CATEGORY_SLUG")
    String slug;

    String description;
}
