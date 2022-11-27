package com.cats.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveHistoryDTO {

	long employeeId;
	String employeeName;
	String startDate;
	String endDate;
	String status;
	String leaveType;
	String reason;
	String managerName;
}
