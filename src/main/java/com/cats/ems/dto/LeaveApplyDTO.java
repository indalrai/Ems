package com.cats.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApplyDTO {

	long id;
	String startDate;
	String endDate;
	String reason;
	long managerId;
	long leaveStatusId;
	long approvedBy;
	long applyTo;
	long leaveTypeId;
		
	
}
