package com.manage.userManagement.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    int id;
    String username;
    String password;
    String name;
    LocalDate dob;
    String role;

    String number;
    String email;
    String address;
    String gender;
}
