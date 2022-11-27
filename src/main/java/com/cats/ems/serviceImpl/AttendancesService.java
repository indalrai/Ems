package com.cats.ems.serviceImpl;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.cats.ems.dto.AttendanceSwipe;
import com.cats.ems.model.Attendance;
import com.cats.ems.model.Employee;

public interface AttendancesService {

	Attendance signIn(HttpServletRequest httpServletRequest);

	Attendance signOut(HttpServletRequest httpServletRequest);

	String totalTime(HttpServletRequest httpServletRequest, String date);

	List<AttendanceSwipe> totalSwipesView(HttpServletRequest httpServletRequest, String localDateTime);

	Map<String, Object> signInDecider(HttpServletRequest httpServletRequest);

	List<Map<Object, Object>> getAllEmployee();

	List<Object> getAttendanceReport(HttpServletRequest httpServletRequest, Long id, String localDateTime,
			String localDateTime1);

	Employee getEmployeeById(long employeeId);

	List<Object> getAllEmployeeReport(HttpServletRequest httpServletRequest, String startDate, String endDate,
			String user);

      String getTotalTimeWithCurrentTime(HttpServletRequest httpServletRequest); 
}