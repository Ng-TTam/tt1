package com.manage.userManagement.mapper;

import com.manage.userManagement.dto.request.ProductRequest;
import com.manage.userManagement.dto.response.ProductResponse;
import com.manage.userManagement.entity.Product;
import com.manage.userManagement.entity.ProductDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductRequest productRequest);

    @Mapping(target = "category", source = "categoryDocument")
    Product toProduct(ProductDocument productDocument);

    ProductResponse toProductResponse(Product product);

    @Mapping(target = "category", source = "categoryDocument")
    ProductResponse toProductResponse(ProductDocument productDocument);

    @Mapping(target = "categoryDocument", source = "category")
    ProductDocument toProductDocument(Product product);

    void updateProduct(@MappingTarget Product product, ProductRequest productRequest);
}
