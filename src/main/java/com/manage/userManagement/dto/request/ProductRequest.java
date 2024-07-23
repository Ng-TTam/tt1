package com.manage.userManagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    @NotBlank(message = "BLANK_PRODUCT_NAME")
    String name;

    @NotBlank(message = "BLANK_PRODUCT_SLUG")
    String slug;

    String brand;

    @NotBlank(message = "BLANK_PRODUCT_SKU")
    String sku;

    @NotNull(message = "BLANK_PRODUCT_PRICE")
    float price;

    @NotNull(message = "BLANK_PRODUCT_QUANTITY")
    int quantity;

    String description;

    @NotBlank(message = "BLANK_CATEGORY_NAME")
    String categoryName;
}
