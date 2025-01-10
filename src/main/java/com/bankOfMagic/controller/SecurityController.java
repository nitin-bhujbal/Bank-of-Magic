package com.bankOfMagic.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SecurityController {
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);
	
	@GetMapping("/home/")
	public String loginSuccess(HttpServletRequest request){
		
		if(request.isUserInRole("ROLE_ADMIN")) {
			logger.info("Admin login successful. Redirecting to dashboard.");
			return "redirect:/admin/dashboard";
		}
		else if(request.isUserInRole("ROLE_CUSTOMER")) {
			logger.info("Customer login successful. Redirecting to customer home.");
			return "redirect:/customer/home";
			
		}
		else {
			logger.warn("No valid role found for the User.");
			return "redirect:/public/error";
		}
		
	}

}
