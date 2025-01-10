package com.bankOfMagic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bankOfMagic.entity.Account;
import com.bankOfMagic.entity.Customer;
import com.bankOfMagic.repository.AccountRepository;
import com.bankOfMagic.repository.CustomerRepository;
import com.bankOfMagic.service.CustomerService;
import com.bankOfMagic.util.MailService;

import jakarta.validation.Valid;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	MailService mailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void saveCustomer(@Valid Customer customer) {
		// TODO Auto-generated method stub
		
		customerRepository.save(customer);
		
	}

	@Override
	public boolean existsByMobileNumber(String mobileNumber) {
		// TODO Auto-generated method stub
		return this.customerRepository.existsByMobileNumber(mobileNumber);
	}

	@Override
	public boolean existsByEmail(String email) {
		// TODO Auto-generated method stub
		return this.customerRepository.existsByEmail(email);
	}

	@Override
	public boolean existsByUsername(String username) {
		// TODO Auto-generated method stub
		return this.customerRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByAadhaarNumber(String aadhaarNumber) {
		// TODO Auto-generated method stub
		return this.customerRepository.existsByAadhaarNumber(aadhaarNumber);
	}

	@Override
	public Customer findByUsername(String username) {
		// TODO Auto-generated method stub
		return this.customerRepository.findByUsername(username);
	}

	@Override
	public Long getTotalCustomersWithRole1() {
		// TODO Auto-generated method stub
		return this.customerRepository.countByIdRole(1);
	}

	@Override
	public long getActiveCustomers() {
		// TODO Auto-generated method stub
		return this.customerRepository.countByIsVerifiedAndIdRoleNot(true, 0);
	}

	@Override
	public long getDeactivatedCustomers() {
		// TODO Auto-generated method stub
		return this.customerRepository.countByIsVerified(false);
	}

	@Override
	public List<Customer> getPendingCustomersWithInactiveAccount() {
		// TODO Auto-generated method stub
		return this.customerRepository.findPendingCustomersWithInactiveAccount();
	}

	@Override
	public boolean approveCustomer(Long customerId) {
		// TODO Auto-generated method stub
		
		try {
			
			Customer customer = this.customerRepository.findById(customerId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid Customer Id"));
			
			Account account = customer.getAccount();
			if(account != null) {
				account.setStatus(true);
				accountRepository.save(account);
			}
			
			customer.setVerified(true);
			customerRepository.save(customer);
			
			String subject = "Welcome to Bank of Magic: Your Account is Ready!";
			String body = "<!DOCTYPE html>" + "<html lang='en'>" + "<head>" + "<style>"
					+ "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; background-color: #f4f4f4; }"
					+ ".container { max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #ffffff; }"
					+ ".header { text-align: center; margin-bottom: 20px; }" + ".logo { max-width: 150px; }"
					+ ".message { margin: 20px 0; }"
					+ ".footer { font-size: 0.9em; text-align: center; color: #777; margin-top: 20px; }"
					+ ".footer-logo { max-width: 150px; }"
					+ ".button { display: inline-block; background-color: #ffffff; color: #0d6efd; text-decoration: none; padding: 10px 20px; border-radius: 5px; font-weight: bold; border: 2px solid #0d6efd; }"
					+ ".button:hover { background-color: #e7f1ff; border-color: #0a58ca; color: #0a58ca; }"
					+ "a { color: #0d6efd; text-decoration: none; }" + "a:hover { color: #0a58ca; }" + "</style>"
					+ "</head>" + "<body>" + "<div class='container'>" + "<div class='header'>"
					+ "<img src='https://drive.google.com/uc?export=view&id=1Dxodxhv07CB-OBhel30xmv4T5PzLcPGA' alt='Bank of Magic Logo' class='logo' />"
					+ "<h2>Welcome to Bank of Magic</h2>" + "</div>" + "<div class='message'>" + "<p>Dear "
					+ customer.getFirstName() + " " + customer.getLastName() + ",</p>"
					+ "<p>We are thrilled to inform you that your account opening request has been approved!</p>"
					+ "<p>You can now access your account and enjoy our seamless banking services. Use the following credentials to log in:</p>"
					+ "<ul>" + "<li><strong>Username:</strong> " + customer.getUsername() + "</li>"
					+ "<li><strong>Password:</strong> (the password you set during registration)</li>" + "</ul>"
					+ "<p>Click below to access your account:</p>"
					+ "<a href='https://bankofmagic.com/login' class='button'>Login to Your Account</a>"
					+ "<p>If you have any questions, feel free to contact our customer support team at <a href='mailto:support@bankofmagic.com'>support@bankofmagic.com</a>.</p>"
					+ "</div>" + "<div class='footer'>" + "<p>Thank you for banking with us!</p>"
					+ "<p><strong>Bank of Magic</strong></p>"
					+ "<img src='https://drive.google.com/uc?export=view&id=1Dxodxhv07CB-OBhel30xmv4T5PzLcPGA' alt='Bank of Magic Logo' class='footer-logo' />"
					+ "</div>" + "</div>" + "</body>" + "</html>";
			
			mailService.sendConfirmationEmail(customer.getEmail(), subject, body);
			return true;
			
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
	}

	@Override
	public boolean rejectCustomer(Long customerId) {
		// TODO Auto-generated method stub
		
		try {
			
			Customer customer = this.customerRepository.findById(customerId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid Customer Id"));
			Account account = customer.getAccount();
			if(account != null) {
				account.setStatus(false);
				accountRepository.save(account);
			}
			
			customer.setVerified(false);
			customerRepository.save(customer);
			
			return true;
			
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
		
	}

	@Override
	public Customer updateCustomerDetails(Long id, String firstName, String middleName, String lastName,
			String mobileNumber, String email, String address, String state, String pincode, String password) {
		// TODO Auto-generated method stub
		return this.customerRepository.findById(id).map(customer -> {
			customer.setFirstName(firstName);
			customer.setMiddleName(middleName);
			customer.setLastName(lastName);
			customer.setMobileNumber(mobileNumber);
			customer.setEmail(email);
			customer.setAddress(address);
			customer.setState(state);
			customer.setPincode(pincode);
			
			//Update password if provided
			if(password != null && !password.isEmpty()) {
				customer.setPassword(passwordEncoder.encode(password));
			}
			
			return this.customerRepository.save(customer);
			
		}).orElse(null);
	}

	@Override
	public Customer findByEmail(String email) {
		// TODO Auto-generated method stub
		return this.customerRepository.findByEmail(email);
	}
	
	
	
}
