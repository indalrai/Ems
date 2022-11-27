package com.cats.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
	long id;
	String taskName;
	String startDate;
	String endDate;
	String estimatedHours;
	String description;
	long employeeId;
	String effortHours;
	String assignTo;
	long taskStatusId; 
	String createTaskDate;
	String updateTaskDate;
}
