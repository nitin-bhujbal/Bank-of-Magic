package com.bankOfMagic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bankOfMagic.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

	@Query("SELECT COUNT(DISTINCT a.branchName) FROM Account a")
	long countDistinctBranch();

	@Query("SELECT COALESCE(SUM(a.balance), 0) FROM Account a")
	Double getTotalMoneyCollected();

	Account findByCustomerId(Long customerId);

	
	
}
