package com.manage.userManagement.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(indexName = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDocument {
    @Id
    @Field(type = FieldType.Keyword)
    String id;
    String name;
    String slug;
    String brand;
    String sku;
    float price;
    int quantity;
    String description;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    LocalDateTime createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    LocalDateTime updatedAt;

    CategoryDocument categoryDocument;
}
