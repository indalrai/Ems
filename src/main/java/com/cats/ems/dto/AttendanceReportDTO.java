package com.cats.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceReportDTO {
    String date;
    String name;
	String signInTime;
	String signOutTime;
	String totalTime;
	String status;
}
