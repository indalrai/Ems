package com.cats.ems.serviceImpl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface AttendanceReportServiceImpl {

	Map<Object, Map<Object, Object>> getAttendanceReport(HttpServletRequest httpServletRequest, long id,
			String localDateTime);
}