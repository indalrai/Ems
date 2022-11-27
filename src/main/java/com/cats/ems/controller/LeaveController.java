package com.cats.ems.controller;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cats.ems.advice.TrackExecutionTime;
import com.cats.ems.dto.EmployeeLeaveInfoDTO;
import com.cats.ems.dto.HolidayCalenderDTO;
import com.cats.ems.dto.LeaveAllEmployeeRoot;
import com.cats.ems.dto.LeaveApplyDTO;
import com.cats.ems.dto.LeaveDTO;
import com.cats.ems.dto.LeaveHistoryDTO;
import com.cats.ems.dto.LeavePendingDTO;
import com.cats.ems.dto.LeaveReviewDTO;
import com.cats.ems.model.GrantedLeave;
import com.cats.ems.model.Leave;
import com.cats.ems.model.LeaveBalance;
import com.cats.ems.model.LeaveType;
import com.cats.ems.repo.EmployeeRepository;
import com.cats.ems.serviceImpl.LeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@CrossOrigin(value = "*")
@SecurityRequirement(name = "cats.emsapi")
@RequestMapping("/leave")
public class LeaveController {

	@Autowired
	LeaveService leaveService;

	@Autowired
	EmployeeRepository employeeRepository;

	@GetMapping("/allLeave")
	@Operation(summary = "${getallLeave.message}")
	@TrackExecutionTime
	public ResponseEntity<LeaveBalance> getLeaveType(HttpServletRequest httpServletRequest) {
		return ResponseEntity.ok(leaveService.getLeave(httpServletRequest));
	}

	@PostMapping("/applyLeave")
	@Operation(summary = "${getLeaveType.message}")
	@TrackExecutionTime
	public ResponseEntity<Leave> applyLeave(HttpServletRequest httpServletRequest,
			@RequestBody LeaveApplyDTO leaveApplyDTO) throws ParseException {
		return ResponseEntity.ok(leaveService.applyLeave(httpServletRequest, leaveApplyDTO));
	}

	@GetMapping("/leaveType")
	@Operation(summary = "${getallleaveType.message}")
	@TrackExecutionTime
	public ResponseEntity<List<LeaveType>> getAllLeaveType() {
		return ResponseEntity.ok(leaveService.getAllLeaveType());
	}

	@GetMapping("/leaveInfo")
	@Operation(summary = "${getEmployeeLeaveInfo.message}")
	@TrackExecutionTime
	public ResponseEntity<List<EmployeeLeaveInfoDTO>> getEmployeeLeaveInfo(@RequestHeader("employeeId") long employeeId,
			@RequestHeader("start_date") String localedate) {
		return ResponseEntity.ok(leaveService.getEmployeeLeaveInfo(employeeId, localedate));
	}

	@GetMapping("/gethistory")
	@Operation(summary = "${gethistory.message}")
	@TrackExecutionTime
	public ResponseEntity<List<LeaveHistoryDTO>> getHistory(HttpServletRequest httpServletRequest) {
		return ResponseEntity.ok(leaveService.getHistoryInfo(httpServletRequest));
	}

	@PostMapping("/approveleave")
	@Operation(summary = "${approve.message}")
	@TrackExecutionTime
	public ResponseEntity<Leave> approveLeave(HttpServletRequest httpServletRequest,
			@RequestHeader("leaveId") long leaveId, @RequestHeader("statusId") long statusId,
			@RequestHeader("approveId") long approveId) throws Exception {
		return ResponseEntity.ok(leaveService.applyLeaveApprove(httpServletRequest, leaveId, statusId));
	}

	@GetMapping("/holidays")
	@Operation(summary = "${holidays.message}")
	@TrackExecutionTime
	public ResponseEntity<List<HolidayCalenderDTO>> getHoliday(HttpServletRequest httpServletReques,
			@RequestHeader(value = "id", required = false) Long employeeId) {
		return ResponseEntity.ok(leaveService.getHoliday(httpServletReques, employeeId));
	}
	
	@PostMapping("/getallleavebymanagerid")
	@Operation(summary = "${getallleavebymanagerid.message}")
	@TrackExecutionTime
	public ResponseEntity<List<LeaveAllEmployeeRoot>> getAllEmployeeByManagerId(HttpServletRequest httpServletRequest, @RequestHeader("start_date") String startDate, @RequestHeader("end_date") String endDate){		
		return ResponseEntity.ok(leaveService.getAllEmployeeByManagerId(httpServletRequest, startDate, endDate));
	}

	@GetMapping("/leavePending")
	@Operation(summary = "${pending.message}")
	@TrackExecutionTime
	public ResponseEntity<List<LeavePendingDTO>> getPendingLeave(HttpServletRequest httpServletRequest) {
		return ResponseEntity.ok(leaveService.getPendingLeave(httpServletRequest));
	}

	@GetMapping("/grantedLeave")
	@Operation(summary = "${grantedLeave.message}")
	@TrackExecutionTime
	public ResponseEntity<GrantedLeave> getGrantedLeaves(HttpServletRequest httpServletRequest) {
		return ResponseEntity.ok(leaveService.getGrantedLeaves(httpServletRequest));
	}

	@PutMapping("/statusUpdate")
	@Operation(summary = "${statusUpdate.message}")
	@TrackExecutionTime
	public ResponseEntity<Leave> updateLeaveStatus(HttpServletRequest httpServletRequest,
			@RequestHeader("id") long leaveId, @RequestHeader("leave_status_id") long statusId) throws ParseException {
		return ResponseEntity.ok(leaveService.updateLeaveStatus(httpServletRequest, leaveId, statusId));
	}
	
	@GetMapping("/review")
	@Operation(summary = "${review.message}")
	@TrackExecutionTime
	public ResponseEntity<List<LeaveReviewDTO>> getManagerReview(HttpServletRequest httpServletRequest){
		return ResponseEntity.ok(leaveService.getManagerReview(httpServletRequest));		
	}

	@GetMapping("/getRemainingLeave")
	@Operation(summary = "${remainingLeave.message}")
	@TrackExecutionTime
	public ResponseEntity<Map<Object, Object>> getRemainingLeaves(HttpServletRequest httpServletRequest,
			@RequestHeader("id") long id) {
		long balance = leaveService.getRemainingLeaves(httpServletRequest, id);
		LinkedHashMap<Object, Object> balMap = new LinkedHashMap<>();
		balMap.put("LeaveBalance", balance);
		return ResponseEntity.ok(balMap);
	}
	
	@PostMapping("/getallleavereveiw")
	@Operation(summary = "${getallleavebymanagerid.message}")
	@TrackExecutionTime
	public ResponseEntity<Map<String, List<LeaveAllEmployeeRoot>>> getAllLeaveReveiw(HttpServletRequest httpServletRequest, @RequestHeader("start_date") String startDate, @RequestHeader("end_date") String endDate){
		return ResponseEntity.ok(leaveService.getAllLeaveReveiw(httpServletRequest, startDate, endDate));
	}
	
	@GetMapping("/getleavebyid")
	@Operation(summary = "${getleavebyid.message}")
	@TrackExecutionTime
	public ResponseEntity<LeaveDTO> getLeaveById(@RequestHeader("id") long id ){
		return ResponseEntity.ok(leaveService.getLeaveById(id));
	}
	
	
}
 