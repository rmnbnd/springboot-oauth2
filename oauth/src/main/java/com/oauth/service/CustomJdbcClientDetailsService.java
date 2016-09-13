package com.oauth.service;

import com.oauth.domain.CustomClientDetails;
import com.oauth.repository.CustomClientDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

public class CustomJdbcClientDetailsService implements ClientDetailsService {

    @Autowired
    private CustomClientDetailsRepository customClientDetailsRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        CustomClientDetails clientDetails = customClientDetailsRepository.findByClientId(clientId);
        if (clientDetails == null) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
        return new BaseClientDetails(clientDetails.getClientId(), clientDetails.getResourceIds(), clientDetails.getScope(),
                clientDetails.getGrantTypes(), clientDetails.getAuthorities());
    }

}
