
package com.client.client.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.client.client.entity.Customer;
import com.client.client.entity.User;
import com.client.client.repo.ClientRepo;
import com.client.client.service.AuthService;
import com.client.client.service.CustomerService;

@Controller
@RequestMapping("/customers")
public class ClientController {
	@Autowired
	ClientRepo clientRepo;
	@Autowired
	AuthService authService;
	
	@Autowired
	CustomerService customerService;
	private int id;
	
	//to go to index page and list all customers
	@GetMapping("/")
    public String viewHomePage(Model model) {
        List<Customer> listcustomer = getAllCustomers();
        model.addAttribute("listcustomer", listcustomer);
        System.out.print("Get / ");    
        return "index";
    }
	//to create a new customer model 
    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("customer", new Customer());
        return "new";
    }
	//after logging to redirect to customerlist page
    @PostMapping("/login")
    public String login(@ModelAttribute User user) {
        
            // Return to the form view if there are errors
    	
        // Handle successful login
        return "redirect:/customers/";
    }
    
    
    // to get login page
	
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login"; // Return your Thymeleaf template name
    }
    //to update a customer details by checkiong if id of customer is present in database
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Optional<Customer> customerData = clientRepo.findById(id);
   	 
        if (customerData.isPresent()) {
    
            model.addAttribute("customer", customerData.get());
       	 System.out.println("In update ");
       	 
            return "UpdateCustomer"; // return the name of the update HTML file
                 	 
        }
        return "error"; // Handle the case where the customer is not found
    }
    
    //if the customer id is present update customer details and save it in database and display it
     @PostMapping("/customers/update/{id}")	
	public String updateCustomer(@PathVariable("id") int id, @RequestBody Customer customer) {
  		System.out.println("Received update request for ID: " + id);
	    Optional<Customer> customerData = clientRepo.findById(id);
	    System.out.println("In update methd");
	    if (customerData.isPresent()) {
	        Customer existingCustomer = customerData.get();
	        existingCustomer.setFirstName(customer.getFirstName());
	        existingCustomer.setLastName(customer.getLastName());
	        existingCustomer.setStreet(customer.getStreet());
	        existingCustomer.setAddress(customer.getAddress());
	        existingCustomer.setCity(customer.getCity());
	        existingCustomer.setState(customer.getState());
	        existingCustomer.setEmail(customer.getEmail());
	        existingCustomer.setPhone(customer.getPhone());
	        clientRepo.save(existingCustomer);
	        System.out.println(" updated");
	        return "redirect:/customers/"; // Redirect to the customer list after updating
	    }
	    return "error"; // Handle the case where the customer is not found
	}

    

	@GetMapping(value="/test")
	public String testEndpoint() {
	    return "Endpoint is working!";
	}
	//to display a single customer details
	@GetMapping(value="/customer/{id}")
	public Optional<Customer> getCustomer(@PathVariable("id") int id) {
		Optional<Customer> customer=clientRepo.findById(id);
	    return customer;
	}
	//to get token if remoteapi authentication is correct
	@GetMapping(value="/gettoken")
	public String getToken() {
//		return authService.authenticate("test@sunbasedata.com", "Test@123");
		String token=authService.authenticate("test@sunbasedata.com", "Test@123");
		System.out.println("token " +token);
		return token;
		
	}
	//to get details of remote customer if authentication is done and if we get the token
	@GetMapping(value="/remoteAPI")
	public String saveCustomerRemoteAPI() {
//		return authService.authenticate("test@sunbasedata.com", "Test@123");
		String token=authService.authenticate("test@sunbasedata.com", "Test@123");
		System.out.println("Token" +token);
		List<Customer> customers=customerService.getCustomerList(token);
		System.out.println(customers);
		for(Customer customer:customers) {
			Customer cust=clientRepo.findByEmail(customer.getEmail());
			System.out.println("Cust "+cust);
			if(cust==null || cust.equals("") ) { 
				clientRepo.save(customer);
			}
			else {
				System.out.println("Customer already exist");
			}
		}
		return "redirect:/customers/";
		
	}
	
	//to create a new customer and save it in database and display it.
	@PostMapping(value = "/create")
	@Transactional
	public String postCustomer(@ModelAttribute Customer customer) {
	Customer customer1=new Customer();
	customer1.setFirstName(customer.getFirstName());
	customer1.setLastName(customer.getLastName());
	customer1.setStreet(customer.getStreet());
	customer1.setAddress(customer.getAddress());
	customer1.setCity(customer.getCity());
	customer1.setState(customer.getState());
	customer1.setEmail(customer.getEmail());
	customer1.setPhone(customer.getPhone());
		System.out.println("Saving customer details");
		clientRepo.save(customer1);
		System.out.println("Saved customer details");
		return "redirect:/customers/";
	}
	
	
	

	// to get all customers details from database
	@GetMapping(value="/customers")

	public List<Customer> getAllCustomers() {
		System.out.println("Get all Customers...");

		List<Customer> customers = new ArrayList<>();
		Iterable<Customer> cs=clientRepo.findAll();
		for(Customer customer:cs) {
			Customer customer1=new Customer();
			customer1.setId(customer.getId());
			customer1.setFirstName(customer.getFirstName());
			customer1.setLastName(customer.getLastName());
			customer1.setStreet(customer.getStreet());
			customer1.setAddress(customer.getAddress());
			customer1.setCity(customer.getCity());
			customer1.setState(customer.getState());
			customer1.setEmail(customer.getEmail());
			customer1.setPhone(customer.getPhone());
			customers.add(customer1);
			System.out.println("Customer List :: " +customers);
		}
		return customers;

	}
	//to delete a customer based on id
	@PostMapping(value="/delete/{id}")
	public String deleteCustomer(@PathVariable("id") int id) {
	    clientRepo.deleteById(id);
	    return "redirect:/customers/"; // Redirect after deletion
	}}



