package com.cats.ems.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
@Entity
@Data
public class Holiday {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	String start;
	String title;
	String type;
	 
	
	
	

}
