package com.oauth.repository;

import com.oauth.domain.CustomClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomClientDetailsRepository extends JpaRepository<CustomClientDetails, Long> {
    CustomClientDetails findByClientId(String clientId);
}
