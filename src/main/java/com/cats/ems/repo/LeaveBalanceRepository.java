package com.cats.ems.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cats.ems.model.LeaveBalance;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long>{

	LeaveBalance findByEmployeeId(long token);

	
}
