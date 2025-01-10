package com.bankOfMagic.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankOfMagic.entity.Account;
import com.bankOfMagic.entity.Customer;
import com.bankOfMagic.entity.FundTransfer;
import com.bankOfMagic.repository.AccountRepository;
import com.bankOfMagic.repository.CustomerRepository;
import com.bankOfMagic.repository.FundTransferRepository;
import com.bankOfMagic.service.FundTransferService;

@Service
public class FundTransferServiceImpl implements FundTransferService{

	
	@Autowired
	private FundTransferRepository fundTransferRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public void fundTransfer(String receiverUsername, Double amount, Long customerId) {
		// TODO Auto-generated method stub
		
		Customer customer = this.customerRepository.findById(customerId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid sender Id"));
		
		Account senderAccount = customer.getAccount();
		
		if(senderAccount == null || senderAccount.getBalance() < amount) {
			throw new IllegalArgumentException("Insufficient fund in sender's account.");
			
		}
		
		senderAccount.setBalance(senderAccount.getBalance() - amount);
		
		accountRepository.save(senderAccount);
		
		FundTransfer transfer = new FundTransfer(amount, LocalDateTime.now(), customer, receiverUsername, "success");
		
		fundTransferRepository.save(transfer);
	}
}
