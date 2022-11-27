package com.cats.ems.dto;

import lombok.Data;

@Data
public class LeaveDTO {
	
	long employeeId;
    String employeeName;
    String fromDate;
    String toDate;
    String leaveType;
    String reason;

}
