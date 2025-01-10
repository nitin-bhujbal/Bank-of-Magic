package com.bankOfMagic.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@RequiredArgsConstructor
@Getter
@Setter
@Data
public class Customer {
	
	@Id
	@GeneratedValue
	(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name", nullable = false)
	@Size(min = 1, max = 10, message = "First name must be between 1 and 10 characters")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name", nullable = false)
	@Size(min = 1, max = 10, message = "Last name must be between 1 and 10 characters")
	private String lastName;

	@Column(name = "mobile_number", nullable = false, unique = true, length = 10)
	@Size(min = 10, max = 10, message = "Enter valid mobile number")
	private String mobileNumber;

	@Column(name = "email", nullable = false, unique = true)
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
	private String email;

	@Column(name = "aadhaar_number", nullable = false, unique = true, length = 12)
	@Size(min = 12, max = 12, message = "Aadhaar number must be in 12 digit")
	private String aadhaarNumber;

	@Column(name = "address", nullable = false)
	@Size(min = 1, max = 30, message = "Address must be between 1 and 30 characters")
	private String address;

	@Column(name = "state", nullable = false)
	@NotBlank(message = "state id is required")
	private String state;

	@Column(name = "pincode", nullable = false, length = 6)
	@NotBlank(message = "Pincode id is required")
	@Size(max = 6, message = "Pincode must be 6 digit number")
	private String pincode;

	@Column(name = "username", nullable = false, unique = true, length = 12)
	@Size(min = 1, max = 12, message = "User id must be between 1 and 12 characters")
	private String username;

	@Column(name = "password", nullable = false)
	@NotBlank(message = "Password is required")
	private String password;

	@Transient
	private String confirmPassword;

	@Column(name = "created_on", nullable = false, updatable = false)
	private LocalDate createdOn;

	@Column(name = "updated_on", nullable = false)
	private LocalDate updatedOn;

	@Column(name = "role_id")
	private Integer idRole;

	@Column(name = "status", nullable = false)
	private boolean isVerified = false;

	@Column(name = "verification_token", nullable = false, unique = true)
	private String verificationToken;
	
	@OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Account account;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<Transaction> transactions;

	@OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
	private List<FundTransfer> senTransfers;

	@OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
	private List<FundTransfer> receivedTransfers;

	
	@PrePersist
	private void prePersist() {
		this.createdOn = LocalDate.now();
		this.updatedOn = LocalDate.now();
		this.verificationToken = java.util.UUID.randomUUID().toString();
	}

}
