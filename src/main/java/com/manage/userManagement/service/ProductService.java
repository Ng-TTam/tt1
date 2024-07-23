package com.manage.userManagement.service;

import com.manage.userManagement.dto.request.ProductRequest;
import com.manage.userManagement.dto.response.ProductResponse;
import com.manage.userManagement.entity.Category;
import com.manage.userManagement.entity.Product;
import com.manage.userManagement.entity.ProductDocument;
import com.manage.userManagement.exception.AppException;
import com.manage.userManagement.exception.ErrorCode;
import com.manage.userManagement.mapper.CategoryMapper;
import com.manage.userManagement.mapper.ProductMapper;
import com.manage.userManagement.repository.CategoryRepository;
import com.manage.userManagement.repository.ProductElasticRepository;
import com.manage.userManagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    
    @Autowired
    ProductElasticRepository productElasticRepository;

    @Autowired
    CategoryRepository categoryRepository;
    
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_PRODUCTS_KEY = "products";
    private static final String REDIS_PRODUCT_KEY = "product";
    private static final long CACHE_PRODUCT_TTL_MINUTES = 30;
    private static final long CACHE_PRODUCTS_TTL_MINUTES = 15;

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest){
        Category category = categoryRepository.findByName(productRequest.getCategoryName())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));

        Product product = productMapper.toProduct(productRequest);
        product.setCategory(category);

        try {
            product = productRepository.save(product);

            ProductDocument productDocument = productMapper.toProductDocument(product);
            productDocument.setCategoryDocument(categoryMapper.toCategoryDocument(category));
            productElasticRepository.save(productDocument); //save in elastic

        } catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.EXISTED_PRODUCT);
        }

        ProductResponse productResponse = productMapper.toProductResponse(product);
        productResponse.setCategory(categoryMapper.toCategoryResponse(category));
        return productResponse;
    }

    @Transactional
    public ProductResponse updateProduct(String slug, ProductRequest productRequest){
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_PRODUCT));
        
        productMapper.updateProduct(product, productRequest);
        product = productRepository.save(product);//update in mysql first
        productElasticRepository.save(productMapper.toProductDocument(product));// update in elastic to sync

        return productMapper.toProductResponse(product);
    }

    public List<ProductResponse> getAllProduct(){
//        HashOperations<String, String, Product> hashOps = redisTemplate.opsForHash();
//        List<Product> products = hashOps.values(REDIS_PRODUCTS_KEY);

//        if (products.isEmpty()) {
//            products = productRepository.findAll();
//
//            if (!products.isEmpty()) {
//                // Lưu vào Redis
//                Map<String, Product> userMap = products.stream().collect(
//                        Collectors.toMap(product -> product.getSlug(), Function.identity()));
//                hashOps.putAll(REDIS_PRODUCTS_KEY, userMap);
//
//                redisTemplate.expire(REDIS_PRODUCTS_KEY, CACHE_PRODUCTS_TTL_MINUTES, TimeUnit.MINUTES);
//            }
//        }
        List<ProductDocument> products = StreamSupport.
                stream(productElasticRepository.findAll().spliterator(),false).toList();
        return products.stream().map(productMapper::toProductResponse).toList();
    }

    public ProductResponse getProduct(String slug) {
//        HashOperations<String, String, Product> hashOps = redisTemplate.opsForHash();
//        Product product = hashOps.get(REDIS_PRODUCT_KEY, slug);

//        if (product == null) {
//            product = productRepository.findById(id).orElse(null);
            Product product = productMapper.toProduct(productElasticRepository.findBySlug(slug).orElse(null));//find product in elastic

//            if (product != null) {
//                // Store in Redis
////                product.setCreateAt(LocalDateTime.parse(product.getCreateAt().format(DATE_FORMATTER)));
//                hashOps.put(REDIS_PRODUCT_KEY, slug, product);
//                redisTemplate.expire(REDIS_PRODUCT_KEY, CACHE_PRODUCT_TTL_MINUTES, TimeUnit.MINUTES);
//            } else {
//                throw new AppException(ErrorCode.NOT_EXISTED_PRODUCT);
//            }
//        }

        return productMapper.toProductResponse(product);
    }

    public List<ProductResponse> searchByKeyword(String keyword){
        List<ProductDocument> productDocuments = productElasticRepository.findByKeyword(keyword);

        return productDocuments.stream().map(productMapper::toProductResponse).toList();
    }

    public List<ProductResponse> searchByKeywordQuantity(String keyword, int quantityMin, int quantityMax){
        List<ProductDocument> productDocuments = productElasticRepository.findByKeywordQuantity(keyword, quantityMin, quantityMax);

        return productDocuments.stream().map(productMapper::toProductResponse).toList();
    }

    public void deleteProduct(String slug){
//        HashOperations<String, String, Product> hashOps = redisTemplate.opsForHash();
//        Product product = hashOps.get(REDIS_PRODUCT_KEY, slug);
//
//        if (product == null) {
          Product  product = productRepository.findBySlug(slug).orElse(null);
//            if (product == null) {
//                throw new AppException(ErrorCode.USER_NOT_EXISTED);
//            }
//        }else{
//            hashOps.delete(REDIS_PRODUCT_KEY, slug);
//        }

        productRepository.deleteBySlug(slug);
        productElasticRepository.deleteBySlug(slug);
    }
}
