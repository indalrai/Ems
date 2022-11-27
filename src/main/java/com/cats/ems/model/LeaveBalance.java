 package com.cats.ems.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class LeaveBalance {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	long casualLeave;
	long sickLeave;
	long earnedLeave;
	long myLeave;
	long restrictedHoliday;
	long companyHoliday;
	long employeeId;
	
}
