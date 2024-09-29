package com.client.client.service;

import org.springframework.beans.factory.annotation.Autowired;
//]import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.client.client.entity.Customer;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_URL = "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";

    public List<Customer> getCustomerList(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Customer[]> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, Customer[].class);
        return List.of(response.getBody());
    }
}
