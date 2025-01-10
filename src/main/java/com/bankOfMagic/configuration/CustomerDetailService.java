package com.bankOfMagic.configuration;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bankOfMagic.entity.Customer;
import com.bankOfMagic.repository.CustomerRepository;

@Service
public class CustomerDetailService implements UserDetailsService{
	
	@Autowired
	CustomerRepository customerRepository;
	
	String[] roles = {"ROLE_ADMIN", "ROLE_CUSTOMER"};
	
	

	public CustomerDetailService(CustomerRepository customerRepository) {
		super();
		this.customerRepository = customerRepository;
	}



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		Customer user = customerRepository.findByUsername(username);
		Set<GrantedAuthority> authorities;
		if(user == null ) {
			throw new UsernameNotFoundException("UserName not found "+username);
		}
		else {
			authorities = new HashSet<>();
			authorities.add(new SimpleGrantedAuthority(roles[user.getIdRole()]));
		}
		
		return new User(user.getUsername(), user.getPassword(), authorities);
	}

}
