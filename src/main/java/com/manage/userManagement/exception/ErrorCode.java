package com.manage.userManagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(301, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(302, "User existed", HttpStatus.BAD_REQUEST),
    INVALID_NAME(303, "Name must be less than 100 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(304, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_NUMBER(305, "Phone number is invalid", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(306, "Email number is invalid", HttpStatus.BAD_REQUEST),
    INVALID_GENDER(307, "Gender must be one of the following: male, female, other", HttpStatus.BAD_REQUEST),
    BLANK_USER(308, "Username is required", HttpStatus.BAD_REQUEST),
    BLANK_NUMBER(309, "Number is required", HttpStatus.BAD_REQUEST),
    BLANK_EMAIL(310, "Email is required", HttpStatus.BAD_REQUEST),
    BLANK_GENDER(311, "Gender is required", HttpStatus.BAD_REQUEST),
    BLANK_PASSWORD(312, "Password is required", HttpStatus.BAD_REQUEST),
    BLANK_USERNAME(313, "Username is required", HttpStatus.BAD_REQUEST),
    DOB_FUTURE(314, "Date of birth must be in the past", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(315, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(316, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    BLANK_CATEGORY_NAME(317,"Category name is required", HttpStatus.BAD_REQUEST),
    BLANK_PRODUCT_NAME(318,"Product name is required", HttpStatus.BAD_REQUEST),
    BLANK_CATEGORY_SLUG(319,"Category slug is required", HttpStatus.BAD_REQUEST),
    BLANK_PRODUCT_SLUG(320,"Product slug is required", HttpStatus.BAD_REQUEST),
    BLANK_PRODUCT_SKU(321,"Product sku is required", HttpStatus.BAD_REQUEST),
    BLANK_PRODUCT_PRICE(322,"Product price is required", HttpStatus.BAD_REQUEST),
    BLANK_PRODUCT_QUANTITY(323,"Product quantity is required", HttpStatus.BAD_REQUEST),
    EXISTED_PRODUCT(324, "Product name or slug existed", HttpStatus.BAD_REQUEST),
    EXISTED_CATEGORY(325, "Category name or slug existed", HttpStatus.BAD_REQUEST),
    NOT_EXISTED_PRODUCT(326, "Product is not existed", HttpStatus.BAD_REQUEST),
    NOT_EXISTED_CATEGORY(327, "Category is not existed", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
