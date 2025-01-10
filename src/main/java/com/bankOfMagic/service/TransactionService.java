package com.bankOfMagic.service;

import java.util.List;

import com.bankOfMagic.entity.Transaction;

public interface TransactionService {

	
	List<Transaction> getTransactionHistory(Long CustomerId);
	
}
