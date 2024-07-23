package com.manage.userManagement.repository;

import com.manage.userManagement.entity.User;
import com.manage.userManagement.entity.UserDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserElasticRepository extends ElasticsearchRepository<UserDocument, String> {
    Optional<UserDocument> findByUsername(String username);

    Optional<UserDocument> findById(String id);

    @Query("{\"multi_match\": {" +
            "\"query\": \"?0\"," +
            "\"fields\": [\"username\", \"name\", \"email\"]" +
            "}}")
    List<UserDocument> findByKeyword(String keyword);
}
