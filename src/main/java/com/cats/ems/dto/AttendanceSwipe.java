package com.cats.ems.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceSwipe {
	long employeeId;
	String signInTime;
	String signOutTime;
	


}
