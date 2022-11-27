package com.cats.ems.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cats.ems.advice.TrackExecutionTime;
import com.cats.ems.model.Employee;
import com.cats.ems.serviceImpl.AttendancesService;

import com.cats.ems.serviceImpl.LeaveService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@CrossOrigin(value = "*")
@SecurityRequirement(name = "cats.emsapi")
@RequestMapping("/employeeDetails")
public class EmployeeController {

	@Autowired
	AttendancesService attendanceService;

	@Autowired
	LeaveService leaveService;

	@GetMapping("/byEmployeeId")
	@Operation(summary = "${employeeId.message}")
	@TrackExecutionTime
	public ResponseEntity<Employee> getEmployeeById(@RequestHeader("employeeId") long employeeId) {
		return new ResponseEntity<Employee>(attendanceService.getEmployeeById(employeeId), HttpStatus.OK);
	}

	@GetMapping("/byManagerId")
	@Operation(summary = "${getEmployeeByManagerId.message}")
	@TrackExecutionTime
	public ResponseEntity<List<Employee>> getEmployeeByManagerId(@RequestHeader("ManagerId") long managerId) {
		return ResponseEntity.ok(leaveService.getAllEmployeeWithManagerId(managerId));
	}
}
