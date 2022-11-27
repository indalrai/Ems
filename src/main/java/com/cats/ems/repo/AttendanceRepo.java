package com.cats.ems.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cats.ems.model.Attendance;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance, Long> {

	
	@Query(value=" select * from attendance a where a.employee_id = :id and TO_TIMESTAMP(a.sign_in_time, 'dd-MM-yyyy HH24:MI:ss') >= \r\n"
			+ " TO_TIMESTAMP(:sign_in_time, 'dd-MM-yyyy HH24:MI:ss') and \r\n"
			+ " TO_TIMESTAMP(a.sign_in_time, 'dd-MM-yyyy HH24:MI:ss') <= TO_TIMESTAMP( :sign_out_time, 'dd-MM-yyyy HH24:MI:ss' )", nativeQuery = true )
	public List<Attendance> findAttendanceByEmployeeId(@Param("id") long employeeId ,@Param("sign_in_time") String startDate ,@Param("sign_out_time") String endDate );

	
	
	@Query(value = "select * from attendance a where a.employee_id = :id and a.sign_in_time >= :sign_in_time  and  a.sign_out_time  IS NULL", nativeQuery = true)
	public List<Attendance> findByEmployeeIdAndSignOutTimeIsNull(@Param("id") long employeeid,
            @Param("sign_in_time") String formatDateTime);

 
	
	@Query(value=" select * from attendance a where a.employee_id = :id and TO_TIMESTAMP(a.sign_in_time, 'dd-MM-yyyy HH24:MI:ss') >= \r\n"
			+ " TO_TIMESTAMP(:sign_in_time, 'dd-MM-yyyy HH24:MI:ss') and \r\n"
			+ " TO_TIMESTAMP(a.sign_in_time, 'dd-MM-yyyy HH24:MI:ss') <= TO_TIMESTAMP( :sign_out_time, 'dd-MM-yyyy HH24:MI:ss' )", nativeQuery = true )
	public List<Attendance> findByEmployeeId(@Param("id") long employeeid,
			@Param("sign_in_time") String startDate,@Param("sign_out_time") String endDate);

	
	@Query(value=" select * from attendance a where a.employee_id = :id and TO_TIMESTAMP(a.sign_in_time, 'dd-MM-yyyy HH24:MI:ss') >= \r\n"
			+ " TO_TIMESTAMP(:sign_in_time, 'dd-MM-yyyy HH24:MI:ss') and \r\n"
			+ " TO_TIMESTAMP(a.sign_out_time, 'dd-MM-yyyy HH24:MI:ss') <= TO_TIMESTAMP( :sign_out_time, 'dd-MM-yyyy HH24:MI:ss' )", nativeQuery = true )

	List<Attendance> findById(@Param("id") long employeeid, @Param("sign_in_time") String startDate,
			@Param("sign_out_time") String endDate);
	
	@Query(value = "select * from attendance a where a.employee_id = :id and a.sign_in_time >= :sign_in_time  and  a.sign_out_time  IS NULL", nativeQuery = true)
    List<Attendance> findByEmployeeIdAndSignInTimeAndSignOutTime(@Param("id") long employeeid,
            @Param("sign_in_time") String dateTime);
 
	
	
}
