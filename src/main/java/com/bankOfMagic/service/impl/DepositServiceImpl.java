package com.bankOfMagic.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankOfMagic.entity.Account;
import com.bankOfMagic.entity.Customer;
import com.bankOfMagic.entity.Deposit;
import com.bankOfMagic.repository.AccountRepository;
import com.bankOfMagic.repository.CustomerRepository;
import com.bankOfMagic.repository.DepositRepository;
import com.bankOfMagic.service.DepositService;

@Service
public class DepositServiceImpl implements DepositService {

	@Autowired
	private DepositRepository depositRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public void depositFunds(Double amount, Long customerId) {
		// TODO Auto-generated method stub
		
		Customer customer = this.customerRepository.findById(customerId).
				orElseThrow(() -> new IllegalArgumentException("Invalid Customer Id"));
		
		Account account = customer.getAccount();
		if(account == null) {
			
			throw new IllegalArgumentException("Customer does not have associated account.");
			
		}
		
		account.setBalance(account.getBalance() + amount);
		accountRepository.save(account);
		
		Deposit deposit = new Deposit(amount, LocalDateTime.now(), customer, "success");
		depositRepository.save(deposit);
		
	}
	
}
