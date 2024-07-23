package com.manage.userManagement.controller;


import com.manage.userManagement.dto.request.CategoryRequest;
import com.manage.userManagement.dto.request.CategoryRequest;
import com.manage.userManagement.dto.response.ApiResponse;
import com.manage.userManagement.dto.response.CategoryResponse;
import com.manage.userManagement.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping
    ApiResponse<CategoryResponse> addCategory(@RequestBody @Valid CategoryRequest categoryRequest){
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(categoryRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<CategoryResponse>> getAll(){
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getAllCategories())
                .build();
    }

    @GetMapping("/{slug}")
    ApiResponse<CategoryResponse> getCategory(@PathVariable String slug){
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.getCategory(slug))
                .build();
    }

    @PutMapping("/{slug}")
    ApiResponse<CategoryResponse> editCategory(@PathVariable String slug, @RequestBody CategoryRequest categoryRequest) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.updateCategory(slug, categoryRequest))
                .build();
    }

    @DeleteMapping("/{slug}")
    ApiResponse<String> deleteCategory(@PathVariable String slug){
        categoryService.deleteCategory(slug);
        return ApiResponse.<String>builder()
                .result("Delete successful")
                .build();
    }
}
