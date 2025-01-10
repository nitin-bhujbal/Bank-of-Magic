package com.bankOfMagic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankOfMagic.entity.Withdraw;

@Repository
public interface WithdrawRepository extends JpaRepository<Withdraw, Long>{

	List<Withdraw> findByCustomerId(Long customerId);

}
