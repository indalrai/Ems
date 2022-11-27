package com.cats.ems.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.cats.ems.model.GrantedLeave;

@Repository
public interface GrantedLeaveRepository extends JpaRepository<GrantedLeave, Long> {

	
	
	@Query(value="select * from granted_leave where employee_id=?",nativeQuery = true)
	GrantedLeave findByEmployeeId(long employeeId);

}
