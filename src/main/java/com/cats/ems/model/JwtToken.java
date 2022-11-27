package com.cats.ems.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class JwtToken {
	
	@Id
	long employeeId;
	String jwtToken;
}
