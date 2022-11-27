package com.cats.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeavePendingDTO {
	long id;
	long employeeId;
	String employeeName;
	String startDate;
	String endDate;
	String status;
	String leaveType;
	String reason;
	String managerName;

}
