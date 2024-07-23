package com.manage.userManagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @NotBlank(message = "BLANK_USER")
    String username;

    @NotBlank(message = "BLANK_PASSWORD")
    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;

    @NotBlank(message = "BLANK_USERNAME")
    @Size(max = 100, message = "INVALID_NAME")
    String name;

    @Past(message = "DOB_FUTURE")
    LocalDate dob;
//    String role;

    @NotBlank(message = "BLANK_NUMBER")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "INVALID_NUMBER")
    String number;

    @NotBlank(message = "BLANK_EMAIL")
    @Email(message = "INVALID_EMAIL")
    String email;

    String address;

    @NotBlank(message = "BLANK_GENDER")
    @Pattern(regexp = "^(male|female|other)$", message = "INVALID_GENDER")
    String gender;
}

