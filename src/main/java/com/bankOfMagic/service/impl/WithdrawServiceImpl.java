package com.bankOfMagic.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankOfMagic.entity.Account;
import com.bankOfMagic.entity.Customer;
import com.bankOfMagic.entity.Withdraw;
import com.bankOfMagic.repository.AccountRepository;
import com.bankOfMagic.repository.CustomerRepository;
import com.bankOfMagic.repository.WithdrawRepository;
import com.bankOfMagic.service.WithdrawService;

@Service
public class WithdrawServiceImpl implements WithdrawService{

	
	@Autowired
	private WithdrawRepository withdrawRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public void withdrawFunds(Double amount, Long customerId) {
		// TODO Auto-generated method stub
		
		Customer customer = this.customerRepository.findById(customerId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Customer Id."));
		
		Account account = customer.getAccount();
		if(account == null) {
			throw new IllegalArgumentException("Customer does not have an associated Account.");			
		}
		
		if(amount < 0) {
			throw new IllegalArgumentException("Withdrawal amount must be greater than zero.)");
		}
		
		if (account.getBalance() < amount) {
			throw new IllegalArgumentException("Insufficient Balance");
		}
		
		account.setBalance(account.getBalance() - amount);
		accountRepository.save(account);
		
		Withdraw withdraw = new Withdraw(amount, LocalDateTime.now(), customer, "success");
		withdrawRepository.save(withdraw);
	}
}
