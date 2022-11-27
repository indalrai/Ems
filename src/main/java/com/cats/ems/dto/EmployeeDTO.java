package com.cats.ems.dto;

import com.cats.ems.model.UserRole;

import lombok.Data;

@Data
public class EmployeeDTO {
	private int employeeId;

	private String employeeName;
	private String employeeEmail;
	private Long employeeMobileNo;
	private String employeeAddress;
	
	private UserRole userRole;

	

}
