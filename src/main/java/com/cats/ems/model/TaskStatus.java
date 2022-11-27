package com.cats.ems.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class TaskStatus {
	@Id
	long id;
	String status;
}
