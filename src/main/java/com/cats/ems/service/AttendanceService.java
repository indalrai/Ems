package com.cats.ems.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cats.ems.config.JwtTokenUtil;
import com.cats.ems.dto.AttendanceReportDTO;
import com.cats.ems.dto.AttendanceSwipe;
import com.cats.ems.model.Attendance;
import com.cats.ems.model.Employee;
import com.cats.ems.repo.AttendanceRepo;
import com.cats.ems.repo.EmployeeRepository;
import com.cats.ems.serviceImpl.AttendancesService;

@Service
public class AttendanceService implements AttendancesService {
	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	AttendanceRepo attendanceRepo;

	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	static long totalDayTime = 0;
	static LocalDateTime startDateTime;
	boolean signInDecider;
	String signInTime;
	String signOutTime;
	long totalminutes = 0;
	long hours = 0;
	long minutes = 0;
	long secs = 0;
	long seconds = 0;
	List<Object> attList = new ArrayList<>();

	public Attendance signIn(HttpServletRequest httpServletRequest) {
		jwtTokenUtil.getToken(httpServletRequest);
		Attendance attendance = new Attendance();
		attendance.setEmployeeId(jwtTokenUtil.getToken(httpServletRequest));
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formatDateTime = now.format(formatter);
		attendance.setSignInTime(formatDateTime);
		return attendanceRepo.save(attendance);
	}

	public Attendance signOut(HttpServletRequest httpServletRequest) {
		Attendance attendance = new Attendance();
		Attendance response = new Attendance();
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formatDateTime = now.format(formatter);
		String[] date = formatDateTime.split(" ");
		String dateTime = date[0] + " " + "00:00:00";
		List<Attendance> list = attendanceRepo
				.findByEmployeeIdAndSignInTimeAndSignOutTime(jwtTokenUtil.getToken(httpServletRequest), dateTime);
		Attendance last = list.get(0);
		attendance.setEmployeeId(last.getEmployeeId());
		attendance.setId(last.getId());
		attendance.setSignInTime(last.getSignInTime());
		attendance.setSignOutTime(formatDateTime);
		response = attendanceRepo.save(attendance);
		return response;

	}

	public Map<String, Object> signInDecider(HttpServletRequest httpServletRequest) {
		Map<String, Object> response = new HashMap<>();
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formatDateTime = now.format(formatter);
		String[] date = formatDateTime.split(" ");
		String dateTime = date[0] + " " + "00:00:00";
		List<Attendance> list = attendanceRepo
				.findByEmployeeIdAndSignOutTimeIsNull(jwtTokenUtil.getToken(httpServletRequest), dateTime);
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
		if(!list.isEmpty())
		{
			list.forEach(e->{
				signInTime=e.getSignInTime();
			});
		}
	LocalDateTime signIn=LocalDateTime.parse(signInTime,formatter);
		response.put("signInTimeStatus", !list.isEmpty() && list.size() > 0);
		if((!list.isEmpty() && list.size() > 0)!=false) {
			response.put("lastSignInTime",signIn.format(formatter1));
		}else {
		String date1=date[0]+" "+"00:00:00";
		LocalDateTime signIn2=LocalDateTime.parse(date1,formatter);
			response.put("lastSignInTime", signIn2.format(formatter1));
		}
		
		return response;

	}


	public String totalTime(HttpServletRequest httpServletRequest, String localedate) {
		String dateTime = localedate + " " + "00:00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		LocalDateTime startDateTime = LocalDateTime.parse(dateTime, formatter);
		LocalDateTime nextDate = startDateTime.plusDays(1);
		String startDate = startDateTime.format(formatter);
		String endDate = nextDate.format(formatter);

		List<Attendance> aList = attendanceRepo.findById(jwtTokenUtil.getToken(httpServletRequest), startDate, endDate);
		aList.forEach(e -> {
			String signInTime = e.getSignInTime();
			String signOutTime = e.getSignOutTime();

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

	@Override
	public List<AttendanceSwipe> totalSwipesView(HttpServletRequest httpServletRequest, String localDateTime) {
		List<AttendanceSwipe> swipesList = new ArrayList<>();
		String dateTime = localDateTime + " " + "00:00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		LocalDateTime startDateTime = LocalDateTime.parse(dateTime, formatter);
		LocalDateTime nextDate = startDateTime.plusDays(1);
		String startDate = startDateTime.format(formatter);

		String endDate = nextDate.format(formatter);
		List<Attendance> aList = attendanceRepo.findByEmployeeId(jwtTokenUtil.getToken(httpServletRequest), startDate,
				endDate);
		aList.forEach(e -> {
			AttendanceSwipe swipes = new AttendanceSwipe();
			swipes.setEmployeeId(jwtTokenUtil.getToken(httpServletRequest));
			if (e.getSignInTime() != null) {
				String signInTime = e.getSignInTime().toString();
				String[] tmp1;
				tmp1 = signInTime.split(" ");
				String signInString = tmp1[1];
				String signString2[];
				signString2 = signInString.split("[.]");
				swipes.setSignInTime(signString2[0]);
			} else {
				swipes.setSignInTime(null);
			}
			if (e.getSignOutTime() != null) {
				String signOutTime = e.getSignOutTime().toString();

				String[] tmp2;
				tmp2 = signOutTime.split(" ");
				String signOutString = tmp2[1];
				String signString2[];
				signString2 = signOutString.split("[.]");

				swipes.setSignOutTime(signString2[0]);
			} else {
				swipes.setSignOutTime(null);
			}
			swipesList.add(swipes);

		});
		return swipesList;
	}

	@Override
	public List<Map<Object, Object>> getAllEmployee() {
		List<Employee> list = employeeRepository.getAllEmployee();
		List<Map<Object, Object>> aList = new ArrayList<>();
		list.forEach(e -> {
			Map<Object, Object> emap1 = new HashMap<>();
			emap1.put("employeeId", e.getEmployeeId());
			emap1.put("name", e.getName());
			aList.add(emap1);
		});
		return aList;
	}

	@Override
	public List<Object> getAttendanceReport(HttpServletRequest httpServletRequest, Long id, String localDateTime,
			String localDateTime1) {

		attList.clear();

		String dateTime = localDateTime + " " + "00:00:00";
		String dateTime1 = localDateTime1 + " " + "00:00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		startDateTime = LocalDateTime.parse(dateTime, formatter);
		LocalDateTime endDateTime = LocalDateTime.parse(dateTime1, formatter);
		while (startDateTime.compareTo(endDateTime) <= 0) {
			LocalDateTime nextDate = startDateTime.plusDays(1);
			String startDate = startDateTime.format(formatter);
			String endDate = nextDate.format(formatter);
			List<Attendance> list;
			if (id == null) {
				list = attendanceRepo.findAttendanceByEmployeeId(jwtTokenUtil.getToken(httpServletRequest), startDate,
						endDate);
			} else {

				list = attendanceRepo.findAttendanceByEmployeeId(id, startDate, endDate);
			}

			if (!list.isEmpty()) {
				AttendanceReportDTO attendanceReportDTO = new AttendanceReportDTO();
				String[] arr = startDate.split(" ");
				attendanceReportDTO.setDate(arr[0]);

				Attendance firstElement = list.get(0);
				Attendance lastElement = list.get(list.size() - 1);
				String[] arr1 = firstElement.getSignInTime().split(" ");
				String signInTime = arr1[1];
				attendanceReportDTO.setSignInTime(signInTime);
				String[] arr2;
				String signOutTime;
				if (lastElement.getSignOutTime() != null) {
					arr2 = lastElement.getSignOutTime().split(" ");
					signOutTime = arr2[1];
				} else {
					signOutTime = null;
				}
				attendanceReportDTO.setSignOutTime(signOutTime);
				long totalTime = 0;
				if (signOutTime != null) {
					totalTime = totalShiftTime(firstElement.getEmployeeId(), arr[0]);
					long hour = (int) totalTime / 60;
					long minutes = (int) totalTime % 60;
					attendanceReportDTO.setTotalTime(hour + " Hours " + minutes + " Minutes");
					if (totalTime >= 570) {
						attendanceReportDTO.setStatus("P");
						attList.add(attendanceReportDTO);
					} else {
						attendanceReportDTO.setStatus("A");
						attList.add(attendanceReportDTO);
					}
				} else {
					attendanceReportDTO.setTotalTime("0");
					attendanceReportDTO.setStatus("A");
					attList.add(attendanceReportDTO);
				}
			} else {

			}
			startDateTime = nextDate;
		}
		return attList;
	}

	public long totalShiftTime(long id, String localedate) {
		String dateTime = localedate + " " + "00:00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		LocalDateTime startDateTime = LocalDateTime.parse(dateTime, formatter);
		LocalDateTime nextDate = startDateTime.plusDays(1);
		String startDate = startDateTime.format(formatter);
		String endDate = nextDate.format(formatter);
		totalminutes = 0;
		List<Attendance> aList = attendanceRepo.findById(id, startDate, endDate);
		aList.forEach(e -> {
			String signInTime = e.getSignInTime();
			String signOutTime = e.getSignOutTime();

			LocalDateTime signInTime1 = LocalDateTime.parse(signInTime, formatter);
			LocalDateTime signOutTime1 = LocalDateTime.parse(signOutTime, formatter);
			Duration duration = Duration.between(signInTime1, signOutTime1);
			totalminutes += duration.toMinutes();
		});

		return totalminutes;
	}

	@Override
	public Employee getEmployeeById(long employeeId) {

		return employeeRepository.findByEmployeeId(employeeId);
	}

	@Override
	public List<Object> getAllEmployeeReport(HttpServletRequest httpServletRequest, String localDateTime,
			String localDateTime1, String user) {

		attList.clear();
		List<Employee> empList;
		long id = jwtTokenUtil.getToken(httpServletRequest);
		empList = employeeRepository.getAllEmployeeWithManagerId(id);
		empList.forEach(e -> {
			String employeeName = e.getName();
			String dateTime = localDateTime + " " + "00:00:00";
			String dateTime1 = localDateTime1 + " " + "00:00:00";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			startDateTime = LocalDateTime.parse(dateTime, formatter);
			LocalDateTime endDateTime = LocalDateTime.parse(dateTime1, formatter);
			while (startDateTime.compareTo(endDateTime) <= 0) {
				LocalDateTime nextDate = startDateTime.plusDays(1);
				String startDate = startDateTime.format(formatter);
				String endDate = nextDate.format(formatter);
				List<Attendance> list = attendanceRepo.findAttendanceByEmployeeId(e.getEmployeeId(), startDate,
						endDate);
				if (!list.isEmpty()) {
					AttendanceReportDTO attendanceReportDTO = new AttendanceReportDTO();
					String[] arr = startDate.split(" ");
					attendanceReportDTO.setDate(arr[0]);
					attendanceReportDTO.setName(employeeName);
					Attendance firstElement = list.get(0);
					Attendance lastElement = list.get(list.size() - 1);
					String[] arr1 = firstElement.getSignInTime().split(" ");
					String signInTime = arr1[1];
					attendanceReportDTO.setSignInTime(signInTime);
					String[] arr2;
					String signOutTime;
					if (lastElement.getSignOutTime() != null) {
						arr2 = lastElement.getSignOutTime().split(" ");
						signOutTime = arr2[1];
					} else {
						signOutTime = null;
					}
					attendanceReportDTO.setSignOutTime(signOutTime);
					long totalTime = 0;
					if (signOutTime != null) {
						totalTime = totalShiftTime(firstElement.getEmployeeId(), arr[0]);
						long hour = (int) totalTime / 60;
						long minutes = (int) totalTime % 60;
						attendanceReportDTO.setTotalTime(hour + " Hours " + minutes + " Minutes");
						if (totalTime >= 570) {
							attendanceReportDTO.setStatus("P");
							attList.add(attendanceReportDTO);
						} else {
							attendanceReportDTO.setStatus("A");
							attList.add(attendanceReportDTO);
						}
					} else {
						attendanceReportDTO.setTotalTime("0");
						attendanceReportDTO.setStatus("A");
						attList.add(attendanceReportDTO);
					}
				} else {

				}
				startDateTime = nextDate;
			}
		});
		return attList;

	}

	@Override
	public String getTotalTimeWithCurrentTime(HttpServletRequest httpServletRequest) {

		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String startDate = now.format(formatter);
		String[] arr = startDate.split(" ");
		String date1 = arr[0] + " " + "00:00:00";
		LocalDateTime nextDate = now.plusDays(1);
		String endDate = nextDate.format(formatter);
		String[] arr1 = endDate.split(" ");
		String date2 = arr1[0] + " " + "00:00:00";
		hours = 0;
		minutes = 0;
		secs = 0;

		List<Attendance> aList = attendanceRepo.findByEmployeeId(jwtTokenUtil.getToken(httpServletRequest), date1,
				date2);
		aList.forEach(e -> {
			String signInTime = e.getSignInTime();
			String signOutTime = e.getSignOutTime();
			LocalDateTime signInTime1 = LocalDateTime.parse(signInTime, formatter);

			if (e.getSignOutTime() != null) {
				LocalDateTime signOutTime1 = LocalDateTime.parse(signOutTime, formatter);
				Duration duration = Duration.between(signInTime1, signOutTime1);
				seconds = duration.getSeconds();
				hours += seconds / 3600;
				minutes += ((seconds % 3600) / 60);
				secs += (seconds % 60);
			} else {
				LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
				Duration duration = Duration.between(signInTime1, currentTime);
				seconds = duration.getSeconds();
				hours += seconds / 3600;
				minutes += ((seconds % 3600) / 60);
				secs += (seconds % 60);

			}
		});
		String timeInHHMMSS = String.format("%02d:%02d:%02d", hours, minutes, secs);
		return timeInHHMMSS;
	}

}
