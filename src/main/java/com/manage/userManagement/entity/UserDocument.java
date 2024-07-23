package com.manage.userManagement.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Getter
@Setter
@Document(indexName = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDocument {
    String id;

    String username;

    String password;

    String name;

    @Field(type = FieldType.Date)
    LocalDate dob;

    String role;

    String number;

    String email;

    String address;

    String gender;
}
