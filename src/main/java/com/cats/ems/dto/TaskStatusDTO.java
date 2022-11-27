package com.cats.ems.dto;

import lombok.Data;

@Data
public class TaskStatusDTO {
	
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
	String status;
	String createTaskDate;
	String updateTaskDate;

}
