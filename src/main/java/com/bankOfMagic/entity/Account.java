package com.bankOfMagic.entity;

import java.time.LocalDate;
import java.util.Random;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@RequiredArgsConstructor
@Getter
@Setter
@Data
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "branch_name", nullable = false)
	@NotBlank(message = "Branch name is required")
	private String branchName;
	
	@Column(name = "account_type", nullable =false)
	@NotBlank(message = "Account type is required")
	private String accountType;
	
	@Column(name = "IFSC_Code", nullable = false)
	private String ifscCode;

	@Column(name = "account_number", nullable = false, unique = true)
	private String accountNumber;

	@Column(name = "balence")
	private Double balance = 0.0;

	@Column(name = "status", nullable = false)
	private boolean status;

	@OneToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@Column(name = "created_on", nullable = false, updatable = false)
	private LocalDate createdOn;

	@Column(name = "updated_on", nullable = false)
	private LocalDate updatedOn;

	@PrePersist
	private void prePresist() {

		if (this.balance == null) {

			this.balance = 0.0;
		}

		this.status = false;

		if (this.accountNumber == null || this.accountNumber.isEmpty()) {

			this.accountNumber = generateAccountNumber();
		}

		if (this.branchName != null) {

			this.ifscCode = generateIfscCode(this.branchName);
		}

		this.createdOn = LocalDate.now();
		this.updatedOn = LocalDate.now();
	}

	private String generateIfscCode(String branchName) {

		String branchCode = branchName.replaceAll("\\s+", "").toUpperCase().substring(0,
				Math.min(branchName.length(), 4));

		return "BOM000" + branchCode;
	}

	private String generateAccountNumber() {

		Random random = new Random();
		long randomNumber = 1000000000L + (long) (random.nextDouble() * 8999999999L);

		return "BOM" + randomNumber;
	}

	
	

}
