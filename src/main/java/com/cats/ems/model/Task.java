package com.cats.ems.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	String taskName;
	String startDate;
	String endDate;
	String estimatedHours;
	String description;
	String effortHours;
	long employeeId;
	String assignTo;
	long taskStatusId;
	String createTaskDate;
	String updateTaskDate;

}
