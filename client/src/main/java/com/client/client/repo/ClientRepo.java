package com.client.client.repo;

//import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.client.client.entity.Customer;

@Repository
public interface ClientRepo extends CrudRepository<Customer, Integer> {
	
	Optional<Customer> findById(int id);
	Customer findByEmail(String email);

}
