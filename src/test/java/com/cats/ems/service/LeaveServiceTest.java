package com.cats.ems.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.cats.ems.config.JwtTokenUtil;
import com.cats.ems.dto.HolidayCalenderDTO;
import com.cats.ems.model.Holiday;
import com.cats.ems.model.Leave;
import com.cats.ems.model.LeaveType;
import com.cats.ems.dto.LeavePendingDTO;
import com.cats.ems.model.Employee;
import com.cats.ems.model.Leave;
import com.cats.ems.model.LeaveType;
import com.cats.ems.model.UserRole;
import com.cats.ems.repo.EmployeeRepository;
import com.cats.ems.model.Employee;
import com.cats.ems.model.GrantedLeave;
import com.cats.ems.repo.EmployeeRepository;
import com.cats.ems.repo.GrantedLeaveRepository;
import com.cats.ems.repo.HolidayRepository;
import com.cats.ems.repo.LeaveApplyRepository;
import com.cats.ems.repo.LeaveTypeRepository;

@ExtendWith(MockitoExtension.class)
public class LeaveServiceTest {

	@Mock
	HolidayRepository holidayRepository;

	@Mock
	EmployeeRepository employeeRepository;

	@Mock
	LeaveApplyRepository leaveApplyRepository;
	@Mock
	LeaveTypeRepository leaveTypeRepository;

	@Mock
	GrantedLeaveRepository grantedLeaveRepository;
	@Mock
	JwtTokenUtil jwtTokenUtil;
	@InjectMocks
	LeaveServiceImpl leaveService;

	@Test
	void Holiday() {
		HttpServletRequest httpServletRequest = null;
		List<HolidayCalenderDTO> calenderDTOs = new ArrayList<>();
		LeaveType leaveType = new LeaveType();
		leaveType.setId(4);
		leaveType.setLeaveType("EL");
		Optional<LeaveType> optional = Optional.of(leaveType);
		List<Holiday> holidays = new ArrayList<>();

		Holiday holiday = new Holiday();
		holiday.setId(2);
		holiday.setTitle(leaveType.getLeaveType());
		holiday.setStart("18-11-2022");
		holiday.setType("RH");
		holidays.add(holiday);
		HolidayCalenderDTO calenderDTO = new HolidayCalenderDTO();
		calenderDTO.setId(holiday.getId());
		calenderDTO.setTitle(holiday.getTitle());
		calenderDTO.setStart(holiday.getStart());
		calenderDTO.setDisplay("block");
		calenderDTO.setBackgroundColor("green");
		calenderDTO.setBorderColor("green");
		calenderDTOs.add(calenderDTO);

		
		List<Leave> leaveList = new ArrayList<>();
		Leave leave = new Leave();
		leave.setApprovedBy(1);
		leave.setEmployeeId(4);
		leave.setEndDate("18-11-2022");
		leave.setId(2);
		leave.setLeaveStatusId(1);
		leave.setLeaveTypeId(5);
		leave.setManagerId(2);
		leave.setReason("Not feel well");
		leave.setStartDate("18-11-2022");
		leaveList.add(leave);

		long id = 1;

		lenient().when(jwtTokenUtil.getToken(httpServletRequest)).thenReturn(id);
		lenient().when(leaveTypeRepository.findById(leave.getLeaveTypeId())).thenReturn(optional);
		lenient().when(leaveApplyRepository.findByEmployeeId(id)).thenReturn(leaveList);
		lenient().when(holidayRepository.findAll()).thenReturn(holidays);

		List<HolidayCalenderDTO> list2 = leaveService.getHoliday(httpServletRequest, 1L);

		assertThat(list2).isEqualTo(calenderDTOs);
	}

	@Test
	void getAllEmployeeWithManagerId() {

		List<Employee> list = new ArrayList<>();
		Employee emp = new Employee();
		emp.setEmployeeId(1);
		emp.setName("Indal");
		emp.setEmail("test@test.gmail");
		emp.setAddress("nagpur");
		emp.setMobile(1234567890L);
		emp.setUserRole(null);
		emp.setManagerId(1);
		list.add(emp);
		when(employeeRepository.getAllEmployeeWithManagerId(1l)).thenReturn(list);
		List<Employee> emp2 = leaveService.getAllEmployeeWithManagerId(1l);
		assertEquals(emp2, list);

	}

	@Test
	void getGrantedLeaves() {

		HttpServletRequest httpServletRequest = null;
		GrantedLeave grant = new GrantedLeave();
		grant.setCasualLeave(2);
		grant.setCompanyHoliday(2);
		grant.setEarnedLeave(2);
		grant.setEmployeeId(1L);
		grant.setMyLeave(2);
		grant.setRestrictedHoliday(2);
		grant.setSickLeave(2);
		when(grantedLeaveRepository.findByEmployeeId(Mockito.anyLong())).thenReturn(grant);
		GrantedLeave grant2 = leaveService.getGrantedLeaves(httpServletRequest);
		assertEquals(grant, grant2);
	}

	@Test
	void getPendingLeave() {
		HttpServletRequest httpServletRequest = null;
		List<Leave> pendingList = new ArrayList<>();
		Leave leave = new Leave();
		leave.setApprovedBy(2);
		leave.setEmployeeId(4);
		leave.setEndDate("18-11-2022");
		leave.setId(12);
		leave.setLeaveStatusId(2);
		leave.setLeaveTypeId(3);
		leave.setManagerId(2);
		leave.setReason("Suffering from fever");
		leave.setStartDate("17-11-2022");
		pendingList.add(leave);

		Employee employee = new Employee();
		employee.setAddress("Faridnagar");
		employee.setEmail("shivaxy2019@Gmail.com");
		employee.setEmployeeId(1);
		employee.setManagerId(2);
		employee.setMobile(123456789l);
		employee.setName("Shiva");
		UserRole userRole = new UserRole();
		userRole.setName("User");
		userRole.setRoleId(1L);
		employee.setUserRole(userRole);

		Employee manager = new Employee();
		manager.setName("neeraj.kumar");

		List<LeavePendingDTO> leavePendingList = new ArrayList<>();
		LeavePendingDTO leavePendingDTO = new LeavePendingDTO();
		leavePendingDTO.setEmployeeId(4);
		leavePendingDTO.setEmployeeName("Shiva");
		leavePendingDTO.setEndDate("18-11-2022");
		leavePendingDTO.setId(12);
		leavePendingDTO.setLeaveType("ML");
		leavePendingDTO.setManagerName("neeraj.kumar");
		leavePendingDTO.setReason("Suffering from fever");
		leavePendingDTO.setStartDate("17-11-2022");
		leavePendingDTO.setStatus("Pending");
		leavePendingList.add(leavePendingDTO);
		LeaveType leaveType = new LeaveType();
		leaveType.setId(3);
		leaveType.setLeaveType("ML");
		Optional<LeaveType> optional = Optional.of(leaveType);
		when(employeeRepository.findByEmployeeId(employee.getManagerId())).thenReturn(manager);
		when(leaveTypeRepository.findById(leave.getLeaveTypeId())).thenReturn(optional);
		when(employeeRepository.findByEmployeeId(jwtTokenUtil.getToken(httpServletRequest))).thenReturn(employee);
		when(leaveApplyRepository.findByEmployeeIdAndStatusId(jwtTokenUtil.getToken(httpServletRequest), 2))
				.thenReturn(pendingList);
		List<LeavePendingDTO> resList = leaveService.getPendingLeave(httpServletRequest);
		assertEquals(leavePendingList, resList);

	}

	
	
}