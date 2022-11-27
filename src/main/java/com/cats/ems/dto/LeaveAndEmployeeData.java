package com.cats.ems.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class LeaveAndEmployeeData {
	@Id
	@Column(name="employee_id")
	Long employeeId;
	@Column(name="name")
	String name;
	@Column(name="start_date")
	String startDate;
	@Column(name="end_date")
	String endDate;
	@Column(name="leave_type_id")
	Long leaveTypeId;
	@Column(name="approved_by")
	Long approvedBy;
	@Column(name="manager_id")
	Long managerId;
	@Column(name="leave_status_id")
	Long leaveStatusId;
	
}
