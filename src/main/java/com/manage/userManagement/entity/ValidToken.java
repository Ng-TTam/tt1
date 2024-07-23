package com.manage.userManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "tokens")
public class ValidToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String token;

    String refreshToken;
    Date expiryTimeRefresh;

}
