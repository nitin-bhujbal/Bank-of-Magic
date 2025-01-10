package com.bankOfMagic.service;

import java.util.List;

import com.bankOfMagic.entity.Customer;

import jakarta.validation.Valid;

public interface CustomerService {
	
	void saveCustomer(@Valid Customer customer);
	
	boolean existsByMobileNumber(String mobileNumber);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsByAadhaarNumber(String aadhaarNumber);

	Customer findByUsername(String username);

	Long getTotalCustomersWithRole1();

	long getActiveCustomers();

	long getDeactivatedCustomers();

	List<Customer> getPendingCustomersWithInactiveAccount();

	boolean approveCustomer(Long customerId);

	boolean rejectCustomer(Long customerId);

	Customer updateCustomerDetails(Long id, String firstName, String middleName, String lastName, String mobileNumber,
			String email, String address, String state, String pincode, String password);

	Customer findByEmail(String email);

}
