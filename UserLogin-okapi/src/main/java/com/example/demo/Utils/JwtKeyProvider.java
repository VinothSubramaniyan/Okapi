package com.example.demo.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class JwtKeyProvider {

	@Value("${jwt.secret-key}")
    private String secretKey;


    public String getSecretKey() {
        return secretKey;
    }
}

