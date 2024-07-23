package com.manage.userManagement.controller;

import com.manage.userManagement.dto.request.ProductRequest;
import com.manage.userManagement.dto.response.ApiResponse;
import com.manage.userManagement.dto.response.ProductResponse;
import com.manage.userManagement.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping
    ApiResponse<ProductResponse> addProduct(@RequestBody @Valid ProductRequest productRequest){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(productRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<ProductResponse>> getAll(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllProduct())
                .build();
    }

    @GetMapping("/{slug}")
    ApiResponse<ProductResponse> getProduct(@PathVariable String slug){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProduct(slug))
                .build();
    }

    @PutMapping("/{slug}")
    ApiResponse<ProductResponse> editProduct(@PathVariable String slug, @RequestBody ProductRequest productRequest) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(slug, productRequest))
                .build();
    }

    @GetMapping("/search")
    ApiResponse<List<ProductResponse>> searchByKey(@RequestParam String keyword){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.searchByKeyword(keyword))
                .build();
    }

    @GetMapping("/searchKeyQuantity")
    ApiResponse<List<ProductResponse>> searchByKeyQuantity(@RequestParam String keyword,
                                                           @RequestParam int min,
                                                           @RequestParam int max){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.searchByKeywordQuantity(keyword, min, max))
                .build();
    }

    @DeleteMapping("/{slug}")
    ApiResponse<String> deleteProduct(@PathVariable String slug){
        productService.deleteProduct(slug);
        return ApiResponse.<String>builder()
                .result("Delete successful")
                .build();
    }

}
