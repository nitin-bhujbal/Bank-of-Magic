package com.bankOfMagic.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "withdraw_data")
@RequiredArgsConstructor
@Getter
@Setter
@Data
public class Withdraw {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double amount;

	@Column(name = "requested_date")
	private LocalDateTime transactionDate;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	private String status;

	public Withdraw(Double amount, LocalDateTime requestedDate, Customer customer, String status) {
		super();
		this.amount = amount;
		this.transactionDate = requestedDate;
		this.customer = customer;
		this.status = status;
	}
}
