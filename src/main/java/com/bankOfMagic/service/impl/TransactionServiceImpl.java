package com.bankOfMagic.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankOfMagic.entity.Account;
import com.bankOfMagic.entity.Deposit;
import com.bankOfMagic.entity.FundTransfer;
import com.bankOfMagic.entity.Transaction;
import com.bankOfMagic.entity.Withdraw;
import com.bankOfMagic.repository.AccountRepository;
import com.bankOfMagic.repository.DepositRepository;
import com.bankOfMagic.repository.FundTransferRepository;
import com.bankOfMagic.repository.WithdrawRepository;
import com.bankOfMagic.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	private DepositRepository depositRepository;

	@Autowired
	private WithdrawRepository withdrawRepository;

	@Autowired
	private FundTransferRepository fundTransferRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public List<Transaction> getTransactionHistory(Long customerId) {
		// TODO Auto-generated method stub
		
		System.out.println("trns");
		System.out.println(customerId);
		List<Transaction> transactionHistory = new ArrayList<>();
		
		int depositCounter = 1;
		int withdrawCounter = 1;
		int fundTransferCounter = 1;
		
		List<Deposit> deposits = this.depositRepository.findByCustomerId(customerId);
		System.out.println(customerId);
		for(Deposit deposit : deposits) {
			Transaction transaction = new Transaction();
			transaction.setId("DEP-" +String.format("%03d", depositCounter++));
			transaction.setType("Deposit");
			transaction.setAmount(deposit.getAmount());
			transaction.setDate(deposit.getTransactionDate());
			System.out.println(deposit.getTransactionDate());
			transaction.setRemainingBalance(getAccountBalance(customerId));
			transaction.setStatus(deposit.getStatus());
			transaction.setCustId(deposit.getCustomer().getId());
			transactionHistory.add(transaction);
			System.out.println(transactionHistory);			
		}
		
		
		List<Withdraw> withdrawals = this.withdrawRepository.findByCustomerId(customerId);
		for(Withdraw withdraw : withdrawals) {
			
			Transaction transaction = new Transaction();
			transaction.setId("WDR-" + String.format("%03d", withdrawCounter++));
			transaction.setType("Withdraw");
			transaction.setAmount(withdraw.getAmount());
			transaction.setDate(withdraw.getTransactionDate());
			transaction.setRemainingBalance(getAccountBalance(customerId));
			transactionHistory.add(transaction);			
		}
		
		List<FundTransfer> fundTransfers = this.fundTransferRepository.findBySenderId(customerId);
		for(FundTransfer fundTransfer : fundTransfers) {
			Transaction transaction = new Transaction();
			transaction.setId("FTR-" +String.format("%03d", fundTransferCounter++));
			transaction.setType("Fund transfer");
			transaction.setAmount(fundTransfer.getAmount());
			transaction.setDate(fundTransfer.getTransactionDate());
			transaction.setRemainingBalance(getAccountBalance(customerId));
			transaction.setCustId(fundTransfer.getSender().getId());
			transactionHistory.add(transaction);
		}
		
		transactionHistory.sort(Comparator.comparing(Transaction::getDate).reversed());
		
		return transactionHistory;
	}
	
	private Double getAccountBalance(Long customerId) {
		Account account = this.accountRepository.findByCustomerId(customerId);
		return account != null ? account.getBalance() : 0.0;
	}
	
}
