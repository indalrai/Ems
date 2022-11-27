package com.cats.ems.dto;

import lombok.Data;

@Data
public class EmployeeLeaveInfoDTO {

	long employeeId;

	String employeeName;

	String fromDate;

	String toDate;

	String leaveType;
	
	String reason;



}
