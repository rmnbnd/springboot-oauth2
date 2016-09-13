package com.oauth.repository;

import com.oauth.domain.CustomCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomCredentialsRepository extends JpaRepository<CustomCredentials, Long> {
    CustomCredentials findByName(String name);
}
