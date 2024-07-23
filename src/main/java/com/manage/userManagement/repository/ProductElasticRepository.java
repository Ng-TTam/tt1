package com.manage.userManagement.repository;

import com.manage.userManagement.entity.ProductDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ProductElasticRepository extends ElasticsearchRepository<ProductDocument, String> {
    Optional<ProductDocument> findBySlug(String slug);

    void deleteBySlug(String slug);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"categoryDocument.name\": \"?0\"}}]}}")
    List<ProductDocument> findByCategoryName(String categoryName);

    @Query("{\"multi_match\": {" +
            "\"query\": \"?0\"," +
            "\"fields\": [\"name\", \"slug\"]" +
            "}}")
    List<ProductDocument> findByKeyword(String keyword);

//    @Query("{\"query\": {\n" +
//            "    \"bool\": {\n" +
//            "      \"must\": [\n" +
//            "        {\n" +
//            "          \"match\": {\n" +
//            "            \"name\": \"?0\"\n" +
//            "          }\n" +
//            "        },\n" +
//            "        {\n" +
//            "          \"range\": {\n" +
//            "            \"quantity\": {\n" +
//            "              \"gte\": \"?1\",\n" +
//            "              \"lte\": \"?2\"\n" +
//            "            }\n" +
//            "          }\n" +
//            "        }\n" +
//            "      ]\n" +
//            "    }\n" +
//            "  }}")
    @Query("{\"bool\": {\"must\": [{\"match\": {\"name\": \"?0\"}}, {\"range\": {\"quantity\": {\"gte\": ?1, \"lte\": ?2}}}]}}")
    List<ProductDocument> findByKeywordQuantity(String keyword, int quantityMin, int quantityMax);
}
