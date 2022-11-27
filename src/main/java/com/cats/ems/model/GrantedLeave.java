package com.cats.ems.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class GrantedLeave {

	@Id
	long employeeId;
	long casualLeave;
	long sickLeave;
	long earnedLeave;
	long myLeave;
	long restrictedHoliday;
	long companyHoliday;
	
}
