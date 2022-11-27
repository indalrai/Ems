
package com.cats.ems.controller;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import com.cats.ems.dto.HolidayCalenderDTO;
import com.cats.ems.dto.LeavePendingDTO;
import com.cats.ems.serviceImpl.LeaveService;

@ExtendWith(MockitoExtension.class)
public class LeaveControllerTest {

	@Mock
	LeaveService leaveServiceImpl;

	@InjectMocks
	LeaveController leaveController;

	@Test
	void Holiday() {
		HttpServletRequest httpServletRequest = null;
		List<HolidayCalenderDTO> calenderDTOs = new ArrayList<>();
		HolidayCalenderDTO calenderDTO = new HolidayCalenderDTO();
		calenderDTO.setId(1);
		calenderDTO.setTitle("New Year's Day");
		calenderDTO.setStart("01-11-2022");
		calenderDTO.setDisplay("block");
		calenderDTO.setBackgroundColor("green");
		calenderDTO.setBorderColor("green");
		calenderDTOs.add(calenderDTO);
		ResponseEntity<List<HolidayCalenderDTO>> expectedResult = ResponseEntity.ok(calenderDTOs);
		when(leaveServiceImpl.getHoliday(httpServletRequest, 1L)).thenReturn(calenderDTOs);
		ResponseEntity<List<HolidayCalenderDTO>> list2 = leaveController.getHoliday(httpServletRequest, 1L);
		assertThat(list2).isEqualTo(expectedResult);
	}

	@Test
	void getPendingLeave() {
		HttpServletRequest httpServletRequest = null;
		List<LeavePendingDTO> list = new ArrayList<>();
		LeavePendingDTO leavePendingDTO = new LeavePendingDTO();
		leavePendingDTO.setEmployeeId(1);
		leavePendingDTO.setEmployeeName("Shiva");
		leavePendingDTO.setEndDate("18-11-2022");
		leavePendingDTO.setId(4);
		leavePendingDTO.setLeaveType("SL");
		leavePendingDTO.setManagerName("Neeraj Kumar");
		leavePendingDTO.setReason("Not Feel Well");
		leavePendingDTO.setStartDate("17-11-2022");
		leavePendingDTO.setStatus("Pending");
		list.add(leavePendingDTO);
		when(leaveServiceImpl.getPendingLeave(httpServletRequest)).thenReturn(list);
		ResponseEntity<List<LeavePendingDTO>> res1 = leaveController.getPendingLeave(httpServletRequest);
		ResponseEntity<List<LeavePendingDTO>> res2 = ResponseEntity.ok(list);
		assertEquals(res1, res2);
	}
}
