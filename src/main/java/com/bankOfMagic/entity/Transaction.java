package com.bankOfMagic.entity;

import java.time.LocalDateTime;

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
@Table(name = "transaction")
@RequiredArgsConstructor
@Getter
@Setter
@Data
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;

	private Double amount;

	private String type;

	private LocalDateTime date;

	private Double remainingBalance;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	private Long custId;

	private String status;

}
