package com.cats.ems.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.cats.ems.dto.AttendanceReportDTO;
import com.cats.ems.dto.AttendanceSwipe;
import com.cats.ems.model.Attendance;
import com.cats.ems.serviceImpl.AttendancesService;

@ExtendWith(MockitoExtension.class)
class AttendanceControllerTest {

	@Mock
	AttendancesService attendanceService;

	@InjectMocks
	AttendanceController attendanceController;
	
	@Mock
	AttendanceReportDTO attendanceReportDTO;

	@Test
	void checkTime() {
		HttpServletRequest httpServletRequest = null;
		String time = "11:37";
		when(attendanceService.totalTime(httpServletRequest, "04-11-2022")).thenReturn(time);
		ResponseEntity<String> time1 = ResponseEntity.ok(time);
		assertEquals(time1, attendanceController.checkTime(httpServletRequest, "04-11-2022"));
	}
	@Test
	void swipes()
	{
		HttpServletRequest httpServletRequest=null;
		List<AttendanceSwipe> swipeList=new ArrayList<>();
		AttendanceSwipe attendanceSwipe=new  AttendanceSwipe();
		attendanceSwipe.setEmployeeId(1);
		attendanceSwipe.setSignInTime("11:23:12");
		attendanceSwipe.setSignOutTime("01:45:13");
		swipeList.add(attendanceSwipe);
		List<AttendanceSwipe> swipeList2=new ArrayList<>();
		AttendanceSwipe attendanceSwipe2=new  AttendanceSwipe();
		attendanceSwipe2.setEmployeeId(1);
		attendanceSwipe2.setSignInTime("11:23:12");
		attendanceSwipe2.setSignOutTime("01:45:13");
		swipeList2.add(attendanceSwipe2);
		ResponseEntity<List<AttendanceSwipe>> expectedResult = ResponseEntity.ok(swipeList2);
		when(attendanceService.totalSwipesView(httpServletRequest,"04-11-2022")).thenReturn(swipeList);
		assertEquals(expectedResult, attendanceController.totalSwipesView(httpServletRequest,"04-11-2022"));
	 	
	}

	@Test
	void signIn() {
		HttpServletRequest httpServletRequest = null;
		Attendance attendance3 = new Attendance();

		Attendance attendance = new Attendance();
		attendance.setEmployeeId(1);
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formatDateTime = now.format(formatter);
		attendance.setSignInTime(formatDateTime);
		attendance.setSignOutTime(null);
		attendance.setEmployeeId(1);
		when(attendanceService.signIn(httpServletRequest)).thenReturn(attendance);
		ResponseEntity<Attendance> attendance2 = attendanceController.signInTime(httpServletRequest);
		attendance3.setId(attendance2.getBody().getId());
		attendance3.setSignInTime(attendance2.getBody().getSignInTime());
		attendance3.setSignOutTime(attendance2.getBody().getSignOutTime());
		attendance3.setEmployeeId(attendance2.getBody().getEmployeeId());
		assertThat(attendance3).isEqualTo(attendance);
	}

	@Test
	void signOut() {
		HttpServletRequest httpServletRequest = null;
		Attendance attendance3 = new Attendance();
		Attendance attendance = new Attendance();
		attendance.setEmployeeId(1);
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formatDateTime = now.format(formatter);
		attendance.setSignInTime(formatDateTime);
		attendance.setSignOutTime(formatDateTime);

		when(attendanceService.signOut(httpServletRequest)).thenReturn(attendance);
		ResponseEntity<Attendance> attendance2 = attendanceController.signOutTime(httpServletRequest);
		attendance3.setId(attendance2.getBody().getId());
		attendance3.setSignInTime(attendance2.getBody().getSignInTime());
		attendance3.setSignOutTime(attendance2.getBody().getSignOutTime());
		attendance3.setEmployeeId(attendance2.getBody().getEmployeeId());

		assertThat(attendance3).isEqualTo(attendance);
	}
	
	@Test
	void getAttendanceReport() {
		
		HttpServletRequest httpServletRequest=null;
		
		List<Object> list =new ArrayList<>();
		AttendanceReportDTO attendanceReportDTO=new AttendanceReportDTO();
		attendanceReportDTO.setSignInTime("11:23:12");
		attendanceReportDTO.setSignOutTime("11:23:12");
		attendanceReportDTO.setDate("09-11-2022");
		attendanceReportDTO.setTotalTime("24");
		Optional<Long> id=Optional.of(1L);
		Optional<String> user=Optional.of("alluser");
		when(attendanceService.getAttendanceReport(httpServletRequest, 1L, "11:23:12","11:23:12")).thenReturn( list);
		ResponseEntity<List<Object>> attendance2 = attendanceController.getAttendanceReport(httpServletRequest,id,"11:23:12","11:23:12",user);
		ResponseEntity<List<Object>> attenndance3=ResponseEntity.ok(list);

		
		assertEquals(attendance2, attenndance3);
	
		
	
	}
}
