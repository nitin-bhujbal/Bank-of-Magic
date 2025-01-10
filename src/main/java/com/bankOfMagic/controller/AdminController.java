package com.bankOfMagic.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bankOfMagic.entity.Customer;
import com.bankOfMagic.service.AccountService;
import com.bankOfMagic.service.CustomerService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AccountService accountService;
	
//	For admin dashboard
	@GetMapping("/admin/dashboard")
	public String adminDashboradHandler(@CurrentSecurityContext(expression = "authentication?.name") String username,
			Model model, HttpSession session) {
		
		Customer customer = this.customerService.findByUsername(username);
		String loginTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy, hh:mm a"));
		
		long totalCustomersWithRole1 = this.customerService.getTotalCustomersWithRole1();
		long activeCustomers = this.customerService.getActiveCustomers();
		long deactivatedCustomers = this.customerService.getDeactivatedCustomers();
		
		long totalBranches = this.accountService.getTotalDistinctBranches();
		double totalMoneyCollected = this.accountService.getTotalMoneyCollected();
		
		List<Customer> pendingcCustomers = this.customerService.getPendingCustomersWithInactiveAccount();
		
		model.addAttribute("loginTime", loginTime);
		model.addAttribute("customer", customer);
		
		model.addAttribute("totalCustomersWithRole1", totalCustomersWithRole1);
		model.addAttribute("activeCustomers", activeCustomers);
		model.addAttribute("deactivatedCustomers", deactivatedCustomers);
		
		model.addAttribute("totalBranches", totalBranches);
		model.addAttribute("totalMoneyCollected", totalMoneyCollected);
		
		model.addAttribute("pendingcCustomers", pendingcCustomers);
		System.out.println(activeCustomers);
		
		String successMessage = (String) session.getAttribute("successMessage");
		String errorMessage = (String) session.getAttribute("errorMessage");
		
		if(successMessage != null) {
			model.addAttribute("successMessage", successMessage);
			session.removeAttribute("successMessage");
		}
		
		if(errorMessage != null) {
			model.addAttribute("errorMessage", errorMessage);
			session.removeAttribute("errorMessage");
		}
		
		return "admin/index";
	}
	
	@PostMapping("/admin/approve")
	public String approveCustomer(@RequestParam("customerId") Long customerId, HttpSession session ) {
		
		boolean isApproved = this.customerService.approveCustomer(customerId);
		if(isApproved) {
			session.setAttribute("successMessage", "Customer approved successfully.");
		}
		else {
			session.setAttribute("errorMessage", "Failed to approve.");
		}
		
		return "redirect:/admin/dashboard";
		
	}
	
	@PostMapping("/admin/reject")
	public String reectCustomer(@RequestParam("customerId") Long customerId, HttpSession session) {	
		
		boolean isReject = this.customerService.rejectCustomer(customerId);
		if(isReject) {
			session.setAttribute("errorMessage", "Customer rejected sucessfully.");
		}
		else {
			session.setAttribute("errorMessage", "Failed to reect customer.");
		}
		
		return "redirect:/admin/dashboard";
		
	}
	
	
}
