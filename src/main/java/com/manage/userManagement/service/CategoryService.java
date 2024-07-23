package com.manage.userManagement.service;

import com.manage.userManagement.dto.request.CategoryRequest;
import com.manage.userManagement.dto.response.CategoryResponse;
import com.manage.userManagement.entity.Category;
import com.manage.userManagement.entity.CategoryDocument;
import com.manage.userManagement.entity.ProductDocument;
import com.manage.userManagement.exception.AppException;
import com.manage.userManagement.exception.ErrorCode;
import com.manage.userManagement.mapper.CategoryMapper;
import com.manage.userManagement.repository.CategoryRepository;
import com.manage.userManagement.repository.ProductElasticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductElasticRepository productElasticRepository;

    @Autowired
    CategoryMapper mapper;

    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest){
        Category category = mapper.toCategory(categoryRequest);

        try{
            categoryRepository.save(category);
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.EXISTED_CATEGORY);
        }

        return mapper.toCategoryResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategory(String slug, CategoryRequest categoryRequest){
        Category category = categoryRepository.findBySlug(slug).orElseThrow(()
                -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));

        List<ProductDocument> productDocuments = productElasticRepository.findByCategoryName(category.getName());//get list product in elastic contain category
        mapper.updateCategory(category, categoryRequest);
        category = categoryRepository.save(category);//update in db mysql

        CategoryDocument categoryDocument = mapper.toCategoryDocument(category);//update in elastic
        productDocuments.forEach(productDocument ->{
            productDocument.setCategoryDocument(categoryDocument);
            productElasticRepository.save(productDocument);
        });
        return mapper.toCategoryResponse(category);
    }

    public List<CategoryResponse> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(mapper::toCategoryResponse).toList();
    }

    public CategoryResponse getCategory(String slug){
        Category category = categoryRepository.findBySlug(slug).orElse(null);
        return mapper.toCategoryResponse(category);
    }

    public void deleteCategory(String slug){
        Category category = categoryRepository.findBySlug(slug).orElseThrow(
                () -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));
        categoryRepository.delete(category);
    }

}
