package com.bankOfMagic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankOfMagic.entity.Deposit;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long>{

	List<Deposit> findByCustomerId(Long customerId);

}
