package com.bankOfMagic.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bankOfMagic.entity.Account;
import com.bankOfMagic.entity.Customer;
import com.bankOfMagic.entity.Transaction;
import com.bankOfMagic.service.AccountService;
import com.bankOfMagic.service.CustomerService;
import com.bankOfMagic.service.DepositService;
import com.bankOfMagic.service.FundTransferService;
import com.bankOfMagic.service.TransactionService;
import com.bankOfMagic.service.WithdrawService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private DepositService depositService;

	@Autowired
	private WithdrawService withdrawService;

	@Autowired
	private FundTransferService fundTransferService;

	@Autowired
	private TransactionService transactionService;
	
	@GetMapping("/customer/home")
	public String customerDashboardHandler(@CurrentSecurityContext(expression = "authentication?.name") String username,
			Model model) {
		
		Customer customer = this.customerService.findByUsername(username);
		String loginTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy, hh:mm a"));
		model.addAttribute("loginTime", loginTime);		
		model.addAttribute("customer", customer);
		
		if(customer.getAccount() != null && customer.getAccount().isStatus()) {
			return "customer/account-pending";
		}
		else if(!customer.isVerified()) {
			return "customer/open-account";
		}
		
		List<Transaction> transactions = this.transactionService.getTransactionHistory(customer.getId());
		List<Transaction> recentTransaction = transactions.size() > 5 ? transactions.subList(0, 5) : transactions;
		
		model.addAttribute("tansactions", recentTransaction);
		
		return "/customer/index";
	}
	
	@PostMapping("/customer/open-account")
	public String openAccountHandler(@CurrentSecurityContext(expression = "authentication?.name") String username,
			@RequestParam("branchName") String branchName, @RequestParam("accountType") String accountType,
			HttpSession session) {
		
		System.out.println("Submitting open accouint form..");
		
		Customer customer = this.customerService.findByUsername(username);
		Account account = new Account();
		account.setBranchName(branchName);
		account.setAccountType(accountType);
		account.setCustomer(customer);
		
		this.accountService.saveAccount(account);
		System.out.println(account);
		
		session.setAttribute("success", 
				"Dear customer your account opening request has been sent to respective branch. You will recieved an email once branch is approve request.");
		
		return "redirect:/custom_login";
	}
	
	@GetMapping("/customer/deposit")
	public String depositFormHandler(@CurrentSecurityContext(expression = "authentication?.name") String username,
			Model model, HttpSession session) {
		
		Customer customer = this.customerService.findByUsername(username);
		String loginTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy, hh:mm a"));
		model.addAttribute("loginTime", loginTime);
		model.addAttribute("customer", customer);
		
		String successMessage = (String) session.getAttribute("success");
		
		if(successMessage != null) {
			model.addAttribute("success", successMessage);
			session.removeAttribute("success");
		}
		
		return "customer/deposit";
	}

	@PostMapping("/customer/deposit")
	public String depositFundsHandler(@RequestParam("amount") Double amount,
			@RequestParam("customerId") Long customerId, HttpSession session) {
		
		System.out.println("deposit");
		this.depositService.depositFunds(amount, customerId);
		
		session.setAttribute("success", "Deposit of amount Rupees "+ amount + " is successful.");
		
		return "redirect:/customer/deposit";
	}
	
	@GetMapping("/customer/withdraw")
	public String withdrawFormHandler(@CurrentSecurityContext(expression = "authentication?.name") String username,
			Model model, HttpSession session) {
		
		Customer customer = this.customerService.findByUsername(username);
		String loginTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy, hh:mm a"));
		model.addAttribute("loginTime", loginTime);
		model.addAttribute("customer", customer);
		
		double currentBalance = customer.getAccount().getBalance();
		
		if(customer.getAccount() != null) {
			model.addAttribute("currentBalance", currentBalance);
		}
		else {
			model.addAttribute("currentBalance", 0.0);
		}
		
		String successMessage = (String) session.getAttribute("success");
		
		if(successMessage != null) {
			
			model.addAttribute("success", successMessage);
			session.removeAttribute("success");
		}
		
		
		return "/customer/withdraw";
	}
	
	@PostMapping("/customer/withdraw")
	public String withdrawFundsHandler(@RequestParam("amount") Double amount, 
			@RequestParam("customerId") Long customerId, HttpSession session) {
		
		this.withdrawService.withdrawFunds(amount, customerId);
		
		session.setAttribute("success", "withdrawal of Rupees "+ amount + " is successful.");
		
		return "redirect:/customer/withdraw";
	}
	
	@GetMapping("/customer/fund-transfer")
	public String fundTransferFormHandler(@CurrentSecurityContext(expression = "authentication?.name") String username,
			Model model, HttpSession session) {
		
		Customer customer = this.customerService.findByUsername(username);
		String loginTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy, hh:mm a"));
		model.addAttribute("loginTime", loginTime);
		model.addAttribute("customer", customer);
		
		double currentBalance = customer.getAccount().getBalance();
		
		if(customer.getAccount() != null) {
			model.addAttribute("currentBalance", currentBalance);
		}
		else {
			model.addAttribute("currentBalance", 0.0);
		}
		
		String successMessage = (String) session.getAttribute("success");
		
		if(successMessage != null) {
			
			model.addAttribute("success", successMessage);
			session.removeAttribute("success");
		}
		
		
		return "/customer/fund-transfer";
	}
	
	@PostMapping("/customer/fund-transfer")
	public String fundTransferFundsHandler(@RequestParam("reciever") String recieverUserName,
			@RequestParam("amount") Double amount, @RequestParam("customerId") Long customerId, HttpSession session) {
		
		this.fundTransferService.fundTransfer(recieverUserName, amount, customerId);
		
		session.setAttribute("success", "Successfully transfer of Ruppes "+ amount + " to" + recieverUserName + ".");
		
		return "redirect:/customer/fund-transfer";
	}
	
	@GetMapping("/customer/transaction-history")
	public String getTransactionHistoryHandler(
			@CurrentSecurityContext(expression = "authentication?.name") String userName, Model model) {
		
		Customer customer = this.customerService.findByUsername(userName);
		String loginTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy, hh:mm a"));
		model.addAttribute("loginTime", loginTime);
		model.addAttribute("customer", customer);
		
		List<Transaction> transactions = this.transactionService.getTransactionHistory(customer.getId());
		
		// Print the transactions before sorting to verify the date
		transactions.forEach(transaction -> System.out.println("before sorting "+ transaction.getDate()));
		
		// Sort transactions by date in ascending order (oldest first)
		transactions.sort(Comparator.comparing(Transaction::getDate));
		
//		Calculate remaining balance strating from the 0.0
		double remainingBalance = 0.0;
		
		for(Transaction transaction : transactions) {
			if("deposit".equals(transaction.getType())) {
				remainingBalance = transaction.getAmount() + remainingBalance;
			}
			else if("Withdraw".equals(transaction.getType()) || "Fund Transfer".equals(transaction.getType())) {
				remainingBalance = transaction.getAmount() - remainingBalance;
			}
		}
		
//		Sort transactions back to descending order (most recent first)
		transactions.sort((t1,t2)-> t1.getDate().compareTo(t2.getDate()));
		
		model.addAttribute("transactions", transactions);
				
		return "/customer/transaction-history";
	}
	
	@GetMapping("/customer/account")
	public String showAccountDetailsHandler(@CurrentSecurityContext(expression = "authentication?.name")
			String userName, Model model, HttpSession session) {
		
		Customer customer = customerService.findByUsername(userName);
		String loginTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy, hh:mm a"));
		model.addAttribute("loginTime", loginTime);
		model.addAttribute("customer", customer);
		
		String successMessage = (String) session.getAttribute("success");
		String errorMessage = (String) session.getAttribute("error");
		
		if(successMessage != null) {
			model.addAttribute("success", successMessage);
			session.removeAttribute("success");
		}
		
		if(errorMessage != null) {
			model.addAttribute("error", errorMessage);
			session.removeAttribute("error");
		}
		
		return "/customer/account";
	}
	
	@PostMapping("/customer/update-details")
	public String updateCustomerDetails(@RequestParam("id") Long id, @RequestParam("firstName") String firstName,
			@RequestParam("middleName") String middleName, @RequestParam("lastName") String lastName,
			@RequestParam("mobileNumber") String mobileNumber, @RequestParam("email") String email,
			@RequestParam("address") String address, @RequestParam("state") String state,
			@RequestParam("pincode") String pincode, @RequestParam("password") String password,
			@RequestParam("confirmPassword") String confirmPassword, HttpSession session) {
		
		if(!password.isEmpty() && !password.equals(confirmPassword)) {
			session.setAttribute("error", "Password didn't match.");
			return "redirect:/customer/account";
		}
		
		Customer updatedCustomer = this.customerService.updateCustomerDetails(id, firstName, 
				middleName, lastName, mobileNumber, email, address, state, pincode, confirmPassword);
		
		if(updatedCustomer != null) {
			session.setAttribute("success", "Details updated succesfully");
		} else {
			session.setAttribute("error", "Failed to update");
		}
		
		return "redirect:/customer/account";
	}
	
	
	
}
