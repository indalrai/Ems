package com.cats.ems.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Leave {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	String startDate;
	String endDate;
	String reason;
	long leaveStatusId;
	long managerId;
	long employeeId;
	long leaveTypeId;
	long approvedBy;
}
