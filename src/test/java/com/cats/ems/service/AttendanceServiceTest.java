package com.cats.ems.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import com.cats.ems.config.JwtTokenUtil;
import com.cats.ems.dto.AttendanceSwipe;
import com.cats.ems.dto.EmployeeDTO;
import com.cats.ems.dto.AttendanceReportDTO;
import com.cats.ems.model.Attendance;
import com.cats.ems.model.AttendanceReport;
import com.cats.ems.model.Employee;
import com.cats.ems.model.UserRole;
import com.cats.ems.repo.AttendanceRepo;
import com.cats.ems.repo.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {

	@Mock
	AttendanceRepo attendanceRepo;

	@Mock
	JwtTokenUtil jwtTokenUtil;

	@Mock
	JwtTokenUtil jwtTokenUtil2;

	@InjectMocks
	AttendanceService attendanceService;
	long totalminutes;

	@Mock
	EmployeeRepository employeeRepository;

	@Test
	void totalTime() {
		HttpServletRequest httpServletRequest = null;
		Attendance attendance = new Attendance();
		attendance.setEmployeeId(1);
		attendance.setId(12);
		attendance.setSignInTime("07-11-2022 11:16:45");
		attendance.setSignOutTime("07-11-2022 12:16:45");
		List<Attendance> alist = new ArrayList<>();
		alist.add(attendance);
		long id = 1;
		when(jwtTokenUtil.getToken(httpServletRequest)).thenReturn(id);
		when(attendanceRepo.findById(jwtTokenUtil.getToken(httpServletRequest), "04-11-2022 00:00:00",
				"05-11-2022 00:00:00")).thenReturn(alist);

		String time1 = calcTime(alist);
		attendanceService.totalTime(httpServletRequest, "04-11-2022");
		assertEquals("1:0", time1);
	}

	String calcTime(List<Attendance> alist) {
		alist.forEach(e -> {
			String signInTime = e.getSignInTime();
			String signOutTime = e.getSignOutTime();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			LocalDateTime signInTime1 = LocalDateTime.parse(signInTime, formatter);
			LocalDateTime signOutTime1 = LocalDateTime.parse(signOutTime, formatter);
			Duration duration = Duration.between(signInTime1, signOutTime1);
			totalminutes += duration.toMinutes();

		});
		int hour = (int) totalminutes / 60;
		int minutes = (int) totalminutes % 60;
		String min = Integer.toString(minutes);
		String hours = Integer.toString(hour);
		totalminutes = 0;
		return hours + ":" + min;

	}

	@Test
	void signIn() {
		HttpServletRequest httpServletRequest = null;
		Attendance attendance = new Attendance();
		attendance.setId(0);
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formatDateTime = now.format(formatter);
		attendance.setSignInTime(formatDateTime);
		attendance.setSignOutTime(null);
		attendance.setEmployeeId(1);
		long id = 1;
		when(attendanceRepo.save(attendance)).thenReturn(attendance);
		when(jwtTokenUtil.getToken(httpServletRequest)).thenReturn(id);
		Attendance attendance3 = attendanceService.signIn(httpServletRequest);
		assertThat(attendance3).isEqualTo(attendance);
	}

	@Test
	void signOut() {
		HttpServletRequest httpServletRequest = null;
		Attendance attendance1 = new Attendance();
		attendance1.setId(0);
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formatDateTime = now.format(formatter);
		String[] strings = formatDateTime.split(" ");
		String string = strings[0];

		attendance1.setSignInTime(formatDateTime + " " + "00:00:00");
		attendance1.setSignOutTime(formatDateTime);
		attendance1.setEmployeeId(1);
		List<Attendance> list = new ArrayList<>();
		list.add(attendance1);
		long id = 1;
		when(attendanceRepo.save(attendance1)).thenReturn(attendance1);
		when(jwtTokenUtil.getToken(httpServletRequest)).thenReturn(id);
		when(attendanceRepo.findByEmployeeIdAndSignInTimeAndSignOutTime(jwtTokenUtil.getToken(httpServletRequest),
				string + " " + "00:00:00")).thenReturn(list);
		Attendance attendance3 = attendanceService.signOut(httpServletRequest);
		attendance1.setSignOutTime(attendance3.getSignOutTime());
		assertThat(attendance3).isEqualTo(attendance1);
	}

	@Test
	void swipes() {
		HttpServletRequest httpServletRequest = null;
		List<Attendance> attendanceList = new ArrayList<>();
		Attendance attendanceSwipe = new Attendance();
		attendanceSwipe.setEmployeeId(1);
		attendanceSwipe.setSignInTime("09-11-2022 11:00:00");
		attendanceSwipe.setSignOutTime("10-11-2022 12:00:00");
		attendanceList.add(attendanceSwipe);

		List<AttendanceSwipe> swipeList1 = new ArrayList<>();
		AttendanceSwipe attendanceSwipe2 = new AttendanceSwipe();
		attendanceSwipe2.setEmployeeId(1);
		attendanceSwipe2.setSignInTime("11:00:00");
		attendanceSwipe2.setSignOutTime("12:00:00");
		swipeList1.add(attendanceSwipe2);
		String startDate = "09-11-2022 00:00:00";
		String endDate = "10-11-2022 00:00:00";
		long id = 1;
		when(jwtTokenUtil.getToken(httpServletRequest)).thenReturn(id);
		when(attendanceRepo.findByEmployeeId(jwtTokenUtil.getToken(httpServletRequest), startDate, endDate))
				.thenReturn(attendanceList);
		List<AttendanceSwipe> swipeList2 = attendanceService.totalSwipesView(httpServletRequest, "09-11-2022");
		assertEquals(swipeList1, swipeList2);
	}

	@Test
	void getAllEmployee() {
		List<Employee> list = new ArrayList<>();
		Employee emp = new Employee();
		emp.setAddress("Noida");
		emp.setEmail("abc@gmail.com");
		emp.setEmployeeId(1);
		emp.setMobile(1234567l);
		emp.setName("Shubham");
		emp.setUserRole(null);
		list.add(emp);
		when(employeeRepository.getAllEmployee()).thenReturn(list);
		List<Map<Object, Object>> emap = attendanceService.getAllEmployee();
		List<Map<Object, Object>> amap = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> listMap = new LinkedHashMap<>();
		listMap.put("employeeId", 1L);
		listMap.put("name", "Shubham");
		amap.add(listMap);
		assertThat(emap).isEqualTo(amap);

	}



	

//@Test
//	void getAttendanceReport() {
//		HttpServletRequest httpServletRequest=null;
//		List<Object> list=new ArrayList<>();
//		List<Attendance> attList=new ArrayList<>();
//		Attendance attendance=new Attendance();
//		attendance.setEmployeeId(4);
//		attendance.setId(2);
//		attendance.setSignInTime("11:22:33");
//		attendance.setSignOutTime("12:23:23");
//		attList.add(attendance);
//		
//		AttendanceReportDTO attendanceReportDTO=new AttendanceReportDTO();
//		attendanceReportDTO.setDate("18-11-2022");
//		attendanceReportDTO.setSignInTime("11:22:33");
//		attendanceReportDTO.setSignOutTime("12:22:23");
//		attendanceReportDTO.setStatus("A");
//		attendanceReportDTO.setTotalTime("1 Hours 0 Minutes");
//		list.add(attendanceReportDTO);
//		when(attendanceRepo.findAttendanceByEmployeeId(jwtTokenUtil.getToken(httpServletRequest),"18-11-2022 00:00:00","19-11-2022 00:00:00")).thenReturn(attList);
//		List<Object> resList=attendanceService.getAttendanceReport(httpServletRequest,null,"18-11-2022","19-11-2022");
//		      
//		assertEquals(list, resList);
//			}

		



	@Test
	void getEmployeeByIdTest() {

		Employee emp = new Employee();
		emp.setEmployeeId(1);
		emp.setName("Indal");
		emp.setEmail("test@test.gmail");
		emp.setAddress("nagpur");
		emp.setMobile(1234567890L);
		emp.setUserRole(null);
		emp.setManagerId(1);
		when(employeeRepository.findByEmployeeId(1l)).thenReturn(emp);
		Employee emp2 = attendanceService.getEmployeeById(1l);
		assertEquals(emp2, emp);

	}

}
