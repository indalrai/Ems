package com.cats.ems.repo;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cats.ems.dto.LeaveAndEmployeeData;
import com.cats.ems.model.Leave;

@Repository
public interface LeaveApplyRepository extends JpaRepository<Leave, Long> {

	@Query(value = " select * from leave a where a.employee_id = :id and TO_TIMESTAMP(a.start_date,'dd-MM-yyyy') = \r\n"
			+ " TO_TIMESTAMP(:localedate, 'dd-MM-yyyy') ", nativeQuery = true)
	List<Leave> findByIdAndDate(@Param("id") long employeeId, @Param("localedate") String startDate);

	@Query(value = "select leave_type from leave_type a where a.id= ?", nativeQuery = true)
	String findLeaveType(@Param("id") long id);

	List<Leave> findByEmployeeId(long employeeId);

	
	@Query(value = "select * from leave a where a.employee_id= :empId and a.leave_status_id= :statusId ",nativeQuery = true)
	List<Leave> findByEmployeeIdAndStatusId(@Param("empId") long id,@Param("statusId") long statusId);

	List<Leave> findByManagerId(long managerId);

	
	@Query(value="select em.name, em.employee_id, l1.approved_by, l1.end_date, l1.leave_type_id, l1.manager_id, l1.start_date, l1.leave_status_id\r\n"
			+ "from Employee em left join ( select * from leave l where\r\n"
			+ "        TO_TIMESTAMP(l.start_date, 'dd-MM-yyyy HH24:MI:ss') >=\r\n"
			+ "             TO_TIMESTAMP( :start_date, 'dd-MM-yyyy HH24:MI:ss')\r\n"
			+ "        and TO_TIMESTAMP(l.start_date, 'dd-MM-yyyy HH24:MI:ss') <= TO_TIMESTAMP(:end_date, 'dd-MM-yyyy HH24:MI:ss')\r\n"
			+ "        and (l.leave_status_id = 1 or l.leave_status_id = 2)) l1 on\r\n"
			+ "    em.employee_id = l1.employee_id where em.manager_id = :id", nativeQuery = true )
	List<LeaveAndEmployeeData> findByManagerId(@Param("id") long managerId ,@Param("start_date") String startDate, @Param("end_date") String endDate);


	
//	@Query(value=" select * from leave a where a.manager_id = :id and TO_TIMESTAMP(a.start_date, 'dd-MM-yyyy HH24:MI:ss') >= \r\n"
//			+ " TO_TIMESTAMP( :start_date, 'dd-MM-yyyy HH24:MI:ss') and \r\n"
//			+ " TO_TIMESTAMP(a.start_date, 'dd-MM-yyyy HH24:MI:ss') <= TO_TIMESTAMP( :end_date, 'dd-MM-yyyy HH24:MI:ss' )", nativeQuery = true )
//	List<Leave> findByManagerId(@Param("id") long managerId ,@Param("start_date") String startDate ,@Param("end_date") String endDate);
}
