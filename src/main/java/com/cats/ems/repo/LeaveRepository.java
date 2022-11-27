package com.cats.ems.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cats.ems.model.LeaveBalance;

@Repository
@Transactional
public interface LeaveRepository extends JpaRepository<LeaveBalance, Long>{
	
	
	@Query("select l from LeaveBalance l where l.employeeId=?1 ")
	LeaveBalance findByEmployeeId(long employeeId);
	
	@Modifying
	@Query("update LeaveBalance u set u.casualLeave=?1,u.sickLeave=?2,u.earnedLeave=?3,u.companyHoliday=?4,u.restrictedHoliday=?5,u.myLeave=?6 where u.employeeId = ?7")
	void updateByEmployeeId(long casualLeave,long sickLeave,long earnedLeave,long companyHoliday,long restrictedHoliday,long myLeave,long employeeId);
}



