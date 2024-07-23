package com.manage.userManagement.repository;

import com.manage.userManagement.entity.ValidToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidTokenRepository extends JpaRepository<ValidToken, String> {
    ValidToken findByRefreshToken(String refreshToken);
    ValidToken findByToken(String token);
}
