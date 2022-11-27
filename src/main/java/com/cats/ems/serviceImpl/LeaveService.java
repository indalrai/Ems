package com.cats.ems.serviceImpl;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.cats.ems.dto.EmployeeLeaveInfoDTO;
import com.cats.ems.dto.HolidayCalenderDTO;
import com.cats.ems.dto.LeaveAllEmployeeRoot;
import com.cats.ems.dto.LeaveApplyDTO;
import com.cats.ems.dto.LeaveDTO;
import com.cats.ems.dto.LeaveHistoryDTO;
import com.cats.ems.dto.LeavePendingDTO;
import com.cats.ems.dto.LeaveReviewDTO;
import com.cats.ems.model.Employee;
import com.cats.ems.model.GrantedLeave;
import com.cats.ems.model.Leave;
import com.cats.ems.model.LeaveBalance;
import com.cats.ems.model.LeaveType;

public interface LeaveService {

	public LeaveBalance getLeave(HttpServletRequest httpServletRequest);

	public Leave applyLeave(HttpServletRequest httpServletRequest, LeaveApplyDTO leaveApplyDTO) throws ParseException;

	public List<LeaveType> getAllLeaveType();
	
	List<LeaveHistoryDTO> getHistoryInfo(HttpServletRequest httpServletRequest);


	List<LeavePendingDTO> getPendingLeave(HttpServletRequest httpServletRequest);

	public Leave applyLeaveApprove(HttpServletRequest httpServletRequest, long leaveId, long statusId) throws Exception;
	
	List<HolidayCalenderDTO> getHoliday(HttpServletRequest httpServletRequest, Long employeeId); 

	List<Employee> getAllEmployeeWithManagerId(long managerId);

	List<EmployeeLeaveInfoDTO> getEmployeeLeaveInfo(long employeeId, String localedate);


	public List<LeaveAllEmployeeRoot> getAllEmployeeByManagerId(HttpServletRequest httpServletRequest, String startDate, String endDate);


	long getRemainingLeaves(HttpServletRequest httpServletRequest, long id);
	
	GrantedLeave getGrantedLeaves(HttpServletRequest httpServletRequest);

	public Leave updateLeaveStatus(HttpServletRequest httpServletRequest, long leaveId, long statusId) throws ParseException;
	
	List<LeaveReviewDTO> getManagerReview(HttpServletRequest httpServletRequest);
	
	public Map<String, List<LeaveAllEmployeeRoot>> getAllLeaveReveiw(HttpServletRequest httpServletRequest,
			String localDateTime, String localDateTime1);
	
	public LeaveDTO getLeaveById(Long id);
}

