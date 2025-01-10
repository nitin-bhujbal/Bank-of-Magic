package com.bankOfMagic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankOfMagic.entity.Account;
import com.bankOfMagic.repository.AccountRepository;
import com.bankOfMagic.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public void saveAccount(Account account) {
		// TODO Auto-generated method stub
		this.accountRepository.save(account);
		
	}

	@Override
	public long getTotalDistinctBranches() {
		// TODO Auto-generated method stub
		return this.accountRepository.countDistinctBranch();
	}

	@Override
	public double getTotalMoneyCollected() {
		// TODO Auto-generated method stub
		
		Double totalMoney = this.accountRepository.getTotalMoneyCollected();
		
		return totalMoney != null ? totalMoney : 0.0;
	}

}
