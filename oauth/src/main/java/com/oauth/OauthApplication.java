package com.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class OauthApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(OauthApplication.class, args);
    }

}
