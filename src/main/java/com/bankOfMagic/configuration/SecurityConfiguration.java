package com.bankOfMagic.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	CustomerDetailService customerDetailService;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests((request) -> request
				.requestMatchers("/","/index","/registration","/forgot", "/save-customer", "/forgot-password").permitAll()
				.requestMatchers("/static/**", "/css/**", "/img/**","/js/**").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/customer/**").hasRole("CUSTOMER")
				.anyRequest()
				.authenticated()
		)
		
		.formLogin((login) -> login
				.loginPage("/custom_login")
				.usernameParameter("username")
				.passwordParameter("password")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/home", true)
				.failureUrl("/invalid")
				.permitAll()
				)
		
		.logout((logout)-> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/custom_login")
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.permitAll()
				);
				
		return http.build();
				
	}
	
	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(getPasswordEncoder());
		provider.setUserDetailsService(customerDetailService);
		
		return provider;
	}
	
}
