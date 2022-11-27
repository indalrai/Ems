package com.cats.ems.repo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cats.ems.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	Employee findByEmployeeId(int id);

	@Query(value = "select * from employee where name=?", nativeQuery = true)
	public Employee findByName(String name);

	Employee findByEmployeeId(long empployeeId);

	@Query(value = "select * from employee order by name asc", nativeQuery = true)
	public List<Employee> getAllEmployee();

	@Query(value = "select * from employee where manager_id=?", nativeQuery = true)
	List<Employee> getAllEmployeeWithManagerId(long id);

	@Query(value = "select * from employee where employee_id=?", nativeQuery = true)
	List<Employee> getEmployeeById();

	@Query(value = " select * from employee  where employee_id = ? ", nativeQuery = true)
	Employee findByEmployeeId1(@Param("employeeId") long employeeId);

	Employee findByManagerId(long token);

	@Query(value = "select * from employee where manager_id=?", nativeQuery = true)
	List<Employee> getAllEmployeeWithManagerId(HttpServletRequest httpServletRequest);

	Employee findByEmployeeId(Long managerId, PageRequest p);

}
