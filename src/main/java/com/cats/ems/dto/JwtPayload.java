package com.cats.ems.dto;

import lombok.Data;

@Data
public class JwtPayload {
 
	private long employeeId;
	private String employeeName;
	private String employeeEmail;
	private long employeeMobileNo;
	private String employeeAddress;
	private String userRole;
	private long managerId;
}
