package com.cats.ems.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cats.ems.advice.TrackExecutionTime;
import com.cats.ems.dto.AttendanceSwipe;
import com.cats.ems.model.Attendance;
import com.cats.ems.serviceImpl.AttendancesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@CrossOrigin(value = "*")
@SecurityRequirement(name = "cats.emsapi")
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	AttendancesService attendanceService;

	@PostMapping("/signin")
	@Operation(summary = "${signin.message}")
	@TrackExecutionTime
	public ResponseEntity<Attendance> signInTime(HttpServletRequest httpServletRequest) {
		return ResponseEntity.ok(attendanceService.signIn(httpServletRequest));
	}

	@PostMapping("/signout")
	@Operation(summary = "${signout.message}")
	@TrackExecutionTime
	public ResponseEntity<Attendance> signOutTime(HttpServletRequest httpServletRequest) {
		return ResponseEntity.ok(attendanceService.signOut(httpServletRequest));
	}

	@PostMapping("/signindecider")
	@Operation(summary = "${signindecider.message}")
	@TrackExecutionTime
	public ResponseEntity<Map<String, Object>> signInDecider(HttpServletRequest httpServletRequest) {
		return ResponseEntity.ok(attendanceService.signInDecider(httpServletRequest));
	}

	@GetMapping("/totalShiftTime")
	@Operation(summary = "${totalShiftTime.message}")
	@TrackExecutionTime
	public ResponseEntity<String> checkTime(HttpServletRequest httpServletRequest,
			@RequestHeader(value = "start_date") String date) {
		return ResponseEntity.ok(attendanceService.totalTime(httpServletRequest, date));
	}

	@PostMapping("/swipeview")
	@Operation(summary = "${swipeview.message}")
	@TrackExecutionTime
	public ResponseEntity<List<AttendanceSwipe>> totalSwipesView(HttpServletRequest httpServletRequest,
			@RequestHeader(value = "start_date") String localDateTime) {
		return ResponseEntity.ok(attendanceService.totalSwipesView(httpServletRequest, localDateTime));
	}

	@GetMapping("/getallemployees")
	@Operation(summary = "${getallemployees.message}")
	@TrackExecutionTime
	public ResponseEntity<List<Map<Object, Object>>> getAllEmployee() {
		return ResponseEntity.ok(attendanceService.getAllEmployee());
	}

	@GetMapping("/getattendancereport")
	@Operation(summary = "${getattendancereport.message}")
	@TrackExecutionTime
	public ResponseEntity<List<Object>> getAttendanceReport(HttpServletRequest httpServletRequest,@RequestHeader(value="id",required = false) Optional<Long> employeeId ,@RequestHeader(value="start_date") String localDateTime, @RequestHeader(value="end_date") String localDateTime1,@RequestHeader(value="users",required = false) Optional<String> users) 
	{
		Long id = null;
		String user=null;
		List<Object> resList=new ArrayList<>();
		if(employeeId.isPresent())
		{
		 id=employeeId.get();
		 resList=attendanceService.getAttendanceReport(httpServletRequest,id,localDateTime,localDateTime1);
		
		}else if (users.isPresent()) {
			user=users.get();
			resList=attendanceService.getAllEmployeeReport(httpServletRequest, localDateTime, localDateTime1,user);
			
		}else
		{
			resList=attendanceService.getAttendanceReport(httpServletRequest, id, localDateTime, localDateTime1);
		}
		 	
		return ResponseEntity.ok(resList);

	}
	@GetMapping("/gettimewithcurrenttime")
	@Operation(summary = "${gettimewithcurrenttime.message}")
	@TrackExecutionTime
	public ResponseEntity<Map<Object, Object>> totalCurrentTime(HttpServletRequest httpServletRequest) {
		String time=attendanceService.getTotalTimeWithCurrentTime(httpServletRequest);
		Map<Object, Object> emap=new LinkedHashMap<>();
		emap.put("totalTime",time);
		return ResponseEntity.ok(emap);
	}
}
