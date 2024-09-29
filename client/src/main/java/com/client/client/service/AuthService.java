package com.client.client.service;
import com.client.client.entity.AuthRequest;
import com.client.client.entity.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private static final String AUTH_URL = "https://qa.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public String authenticate(String loginId, String password) {
        AuthRequest request = new AuthRequest();
        request.setLogin_id(loginId);
        request.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AuthRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(AUTH_URL, HttpMethod.POST, entity, String.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            // Extract the JSON part of the response
            String jsonPart = responseBody.split(",")[0]; // Get the JSON part before the comma
            System.out.println(jsonPart);
            String t= jsonPart.split(":")[1];
         String token= t.replace("\"", "").replace("}", "").trim();
         System.out.println(token);
         return token;
        } else {
            throw new RuntimeException("Authentication failed: " + response.getStatusCode());
        }
    }
}
