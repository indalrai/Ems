package com.cats.ems.dto;

import lombok.Data;

@Data
public class HolidayCalenderDTO {
	
	long id;
	String start;
	String end;
	String title;
	String display;
	String backgroundColor;
	String borderColor;
}
