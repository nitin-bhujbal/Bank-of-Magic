package com.bankOfMagic.service;

public interface FundTransferService {
	
	public void fundTransfer(String receiverUsername, Double amount, Long customerId);

}
