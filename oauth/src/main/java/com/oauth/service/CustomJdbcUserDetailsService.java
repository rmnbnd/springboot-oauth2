package com.oauth.service;

import com.oauth.domain.CustomCredentials;
import com.oauth.repository.CustomCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomJdbcUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomCredentialsRepository customCredentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomCredentials credentials = customCredentialsRepository.findByName(username);
        if (credentials == null) {
            throw new UsernameNotFoundException("User " + username + " not found in database.");
        }
        return new User(credentials.getName(), credentials.getPassword(), true, true, true, true, credentials.getAuthorities());
    }

}
