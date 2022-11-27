package com.cats.ems.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cats.ems.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	Optional<Task> findById(long id);

	List<Task> findByEmployeeId(long token);

	@Query(value=" select * from task t where t.employee_id = :id and TO_TIMESTAMP(t.start_date, 'dd-MM-yyyy HH24:MI:ss') >= \r\n"
			+ " TO_TIMESTAMP(:start_date, 'dd-MM-yyyy HH24:MI:ss') and \r\n"
			+ " TO_TIMESTAMP(t.start_date, 'dd-MM-yyyy HH24:MI:ss') <= TO_TIMESTAMP( :end_date, 'dd-MM-yyyy HH24:MI:ss' )", nativeQuery = true )

	List<Task> findByEmployeeId(@Param("id") long employeeid, @Param("start_date") String startDate,
			@Param("end_date") String endDate);

	@Query(value="Select * from task t where t.employee_id in (select e.employee_id from employee e where e. manager_id=:id) order by update_task_date DESC",nativeQuery = true)
	List<Task> findByManagerId(@Param("id") long token);

	

	
	
	

	

	
}
