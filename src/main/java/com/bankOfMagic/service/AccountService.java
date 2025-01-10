package com.bankOfMagic.service;

import com.bankOfMagic.entity.Account;

public interface AccountService {

	
	void saveAccount(Account account);

	long getTotalDistinctBranches();

	double getTotalMoneyCollected();

}
