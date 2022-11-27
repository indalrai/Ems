package com.cats.ems.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cats.ems.config.JwtTokenUtil;
import com.cats.ems.dto.EmployeeLeaveInfoDTO;
import com.cats.ems.dto.HolidayCalenderDTO;
import com.cats.ems.dto.LeaveAllEmployee;
import com.cats.ems.dto.LeaveAllEmployeeRoot;
import com.cats.ems.dto.LeaveAndEmployeeData;
import com.cats.ems.dto.LeaveApplyDTO;
import com.cats.ems.dto.LeaveDTO;
import com.cats.ems.dto.LeaveHistoryDTO;
import com.cats.ems.dto.LeavePendingDTO;
import com.cats.ems.dto.LeaveReviewDTO;
import com.cats.ems.model.Employee;
import com.cats.ems.model.GrantedLeave;
import com.cats.ems.model.Holiday;
import com.cats.ems.model.Leave;
import com.cats.ems.model.LeaveBalance;
import com.cats.ems.model.LeaveStatus;
import com.cats.ems.model.LeaveType;
import com.cats.ems.repo.EmployeeRepository;
import com.cats.ems.repo.EmployeeRepositoryEntityManager;
import com.cats.ems.repo.GrantedLeaveRepository;
import com.cats.ems.repo.HolidayRepository;
import com.cats.ems.repo.LeaveApplyRepository;
import com.cats.ems.repo.LeaveBalanceRepository;
import com.cats.ems.repo.LeaveRepository;
import com.cats.ems.repo.LeaveStatusRepository;
import com.cats.ems.repo.LeaveTypeRepository;
import com.cats.ems.serviceImpl.LeaveService;

@Service
public class LeaveServiceImpl implements LeaveService {

	static LocalDateTime startDateTime;

	@Autowired
	LeaveTypeRepository leaveTypeRepository;

	@Autowired
	HolidayRepository holidayRepository;

	@Autowired
	LeaveRepository leaveRepository;

	@Autowired
	EmployeeRepositoryEntityManager employeeRepositoryEntityManager;

	@Autowired
	LeaveApplyRepository leaveApplyRepository;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	LeaveBalanceRepository leaveBalanceRepository;

	@Autowired
	LeaveStatusRepository leaveStatusRepository;

	@Autowired
	GrantedLeaveRepository grantedLeaveRepository;

	@Autowired
	EmployeeRepository employeeRepository;
	List<LeavePendingDTO> resPendingList = new ArrayList<>();
	List<LeaveHistoryDTO> leaveHistoryDtoList = new ArrayList<>();

	List<LeaveAllEmployeeRoot> allEmployeesRoot = new ArrayList<>();

	@Override
	public LeaveBalance getLeave(HttpServletRequest httpServletRequest) {
		return leaveRepository.findByEmployeeId(jwtTokenUtil.getToken(httpServletRequest));
	}

	@Override
	public Leave applyLeave(HttpServletRequest httpServletRequest, LeaveApplyDTO leaveApplyDTO) throws ParseException {
		Leave leaveApply = new Leave();

		leaveApply.setStartDate(leaveApplyDTO.getStartDate());
		leaveApply.setEndDate(leaveApplyDTO.getEndDate());
		leaveApply.setReason(leaveApplyDTO.getReason());
		leaveApply.setManagerId(leaveApplyDTO.getApplyTo());
		leaveApply.setManagerId(leaveApplyDTO.getManagerId());
		leaveApply.setEmployeeId(jwtTokenUtil.getToken(httpServletRequest));
		leaveApply.setLeaveStatusId(leaveApplyDTO.getLeaveStatusId());
		leaveApply.setLeaveTypeId(leaveApplyDTO.getLeaveTypeId());
		leaveApply.setLeaveStatusId(2);
		leaveApply.setApprovedBy(leaveApplyDTO.getApprovedBy());
		Optional<LeaveType> leaveType = leaveTypeRepository.findById(leaveApplyDTO.getLeaveTypeId());

		LeaveBalance leaveBalance = leaveRepository.findByEmployeeId(jwtTokenUtil.getToken(httpServletRequest));
		LeaveBalance leaveBalanceUpdated = new LeaveBalance();
		String string = leaveType.get().getLeaveType();
		SimpleDateFormat myFormat1 = new SimpleDateFormat("dd-MM-yyyy");
		Date date1 = myFormat1.parse(leaveApply.getStartDate());
		Date date2 = myFormat1.parse(leaveApply.getEndDate());
		String stDate = leaveApplyDTO.getStartDate() + " " + "00:00:00";
		String enDate = leaveApplyDTO.getEndDate() + " " + "00:00:00";
		DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		LocalDateTime startDate = LocalDateTime.parse(stDate, myFormat);
		LocalDateTime endDate = LocalDateTime.parse(enDate, myFormat);
//		List<Holiday> holidays=holidayRepository.findAll();
//		holidays.forEach(e->{
//			String holidayDate=e.getStart()+" "+"00:00:00";
//			
//			LocalDateTime dateTime=LocalDateTime.parse(holidayDate, myFormat);
//			while (startDateTime.compareTo(endDate) <= 0) {
//				LocalDateTime dateTime1DateTime=
//			}
//		});
		long day = 0;
		LocalDateTime startDateTime = startDate;
		while (startDateTime.compareTo(endDate) <= 0) {
			LocalDateTime nextDate = startDateTime.plusDays(1);

			DayOfWeek expected = startDateTime.getDayOfWeek();
			DayOfWeek day1 = DayOfWeek.SATURDAY;
			DayOfWeek day2 = DayOfWeek.SUNDAY;
			if (expected == day1 || expected == day2) {
				day++;
			}
			startDateTime = nextDate;
		}

		long diff = date2.getTime() - date1.getTime();
		float days = (diff / (1000 * 60 * 60 * 24));
		long aValue = (long) days;
		long leftDay = aValue - day;
		long leftDays = leftDay + 1;

		if (string.equalsIgnoreCase("Casual Leave")) {
			leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave() - leftDays);
			leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());
			leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
			leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
			leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
			leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
			leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());

		} else if (string.equalsIgnoreCase("Sick Leave")) {
			leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave() - leftDays);
			leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
			leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
			leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
			leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
			leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
			leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());

		} else if (string.equalsIgnoreCase("My Leave")) {
			leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave() - leftDays);

			leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
			leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
			leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
			leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
			leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());
			leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

		} else if (string.equalsIgnoreCase("Restricted Holiday")) {
			leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday() - leftDays);

			leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
			leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
			leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
			leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
			leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
			leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

		} else if (string.equalsIgnoreCase("Company Holiday")) {
			leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday() - leftDays);

			leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());
			leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
			leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
			leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
			leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
			leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

		} else if (string.equalsIgnoreCase("Earn Leave")) {
			leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave() - leftDays);

			leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());
			leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
			leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
			leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
			leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
			leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

		}

		leaveRepository.updateByEmployeeId(leaveBalanceUpdated.getCasualLeave(), leaveBalanceUpdated.getSickLeave(),
				leaveBalanceUpdated.getEarnedLeave(), leaveBalanceUpdated.getCompanyHoliday(),
				leaveBalanceUpdated.getRestrictedHoliday(), leaveBalanceUpdated.getMyLeave(),
				leaveBalanceUpdated.getEmployeeId());

		return leaveApplyRepository.save(leaveApply);

	}

	@Override
	public List<LeaveType> getAllLeaveType() {
		return leaveTypeRepository.findAll();
	}

	@Override
	public List<Employee> getAllEmployeeWithManagerId(long managerId) {
		return employeeRepository.getAllEmployeeWithManagerId(managerId);
	}

	@Override

	public Leave applyLeaveApprove(HttpServletRequest httpServletRequest, long leaveId, long statusId)

			throws ParseException {
		Leave leaveApply = leaveApplyRepository.findById(leaveId).get();

		if (statusId == 1) {
			leaveApply.setLeaveStatusId(statusId);

			leaveApply.setApprovedBy(jwtTokenUtil.getToken(httpServletRequest));
		} else {
			leaveApply.setLeaveStatusId(statusId);
			leaveApply.setApprovedBy(jwtTokenUtil.getToken(httpServletRequest));

			Optional<LeaveType> leaveType = leaveTypeRepository.findById(leaveApply.getLeaveTypeId());

			LeaveBalance leaveBalance = leaveRepository.findByEmployeeId(leaveApply.getEmployeeId());
			LeaveBalance leaveBalanceUpdated = new LeaveBalance();
			String string = leaveType.get().getLeaveType();
			SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date1 = myFormat.parse(leaveApply.getStartDate());
			Date date2 = myFormat.parse(leaveApply.getEndDate());
			long diff = date2.getTime() - date1.getTime();
			float days = (diff / (1000 * 60 * 60 * 24));
			long aValue = (long) days;

			if (string.equalsIgnoreCase("Casual Leave")) {
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave() - aValue);

				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());

			} else if (string.equalsIgnoreCase("Sick Leave")) {
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave() - aValue);

				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());

			} else if (string.equalsIgnoreCase("My Leave")) {
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave() - aValue);

				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

			} else if (string.equalsIgnoreCase("Restricted Holiday")) {
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday() - aValue);

				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

			} else if (string.equalsIgnoreCase("Company Holiday")) {
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday() - aValue);

				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

			} else if (string.equalsIgnoreCase("Earn Leave")) {
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave() - aValue);

				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

			}

			leaveRepository.updateByEmployeeId(leaveBalanceUpdated.getCasualLeave(), leaveBalanceUpdated.getSickLeave(),
					leaveBalanceUpdated.getEarnedLeave(), leaveBalanceUpdated.getCompanyHoliday(),
					leaveBalanceUpdated.getRestrictedHoliday(), leaveBalanceUpdated.getMyLeave(),
					leaveBalanceUpdated.getEmployeeId());
		}
		return leaveApplyRepository.save(leaveApply);

	}

	public List<LeaveStatus> getApproveStatus(HttpServletRequest httpServletRequest) {
		List<LeaveStatus> leaveStatus2 = new ArrayList<>();
		List<Leave> leaves = leaveApplyRepository.findByEmployeeId(jwtTokenUtil.getToken(httpServletRequest));
		leaves.forEach(e -> {
			{
				LeaveStatus leaveStatus = new LeaveStatus();
				LeaveStatus leaveStatus3 = leaveStatusRepository.findById(e.getLeaveStatusId()).get();
				leaveStatus.setId(e.getId());
				leaveStatus.setString(leaveStatus3.getString());
				leaveStatus2.add(leaveStatus);
			}
		});
		return leaveStatus2;

	}

	@Override
	public List<LeaveHistoryDTO> getHistoryInfo(HttpServletRequest httpServletRequest) {
		leaveHistoryDtoList.clear();
		List<Leave> leaves = leaveApplyRepository.findByEmployeeId(jwtTokenUtil.getToken(httpServletRequest));
		leaves.forEach(e -> {
			if (e.getLeaveStatusId() != 2) {
				LeaveHistoryDTO leaveHistoryDTO = new LeaveHistoryDTO();
				leaveHistoryDTO.setEmployeeId(e.getEmployeeId());
				long employeeId = e.getEmployeeId();
				Employee emp = employeeRepository.findByEmployeeId(employeeId);
				leaveHistoryDTO.setEmployeeName(emp.getName());
				long leaveTypeId = e.getLeaveTypeId();
				LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId).get();
				leaveHistoryDTO.setLeaveType(leaveType.getLeaveType());
				long managerId = e.getManagerId();
				Employee manager = employeeRepository.findByEmployeeId(managerId);
				leaveHistoryDTO.setManagerName(manager.getName());
				leaveHistoryDTO.setReason(e.getReason());
				leaveHistoryDTO.setStartDate(e.getStartDate());
				leaveHistoryDTO.setEndDate(e.getEndDate());
				long statusId = e.getLeaveStatusId();
				LeaveStatus leaveStatus = leaveStatusRepository.findById(statusId).get();
				leaveHistoryDTO.setStatus(leaveStatus.getString());
				leaveHistoryDtoList.add(leaveHistoryDTO);
			}

		});

		return leaveHistoryDtoList;
	}

	@Override
	public List<HolidayCalenderDTO> getHoliday(HttpServletRequest httpServletRequest, Long employeeId) {

		List<HolidayCalenderDTO> calenderDTOs = new ArrayList<>();
		List<Holiday> holidays = holidayRepository.findAll();
		holidays.forEach(e -> {
			HolidayCalenderDTO calenderDTO = new HolidayCalenderDTO();
			calenderDTO.setId(e.getId());
			calenderDTO.setTitle(e.getTitle());
			calenderDTO.setStart(e.getStart());
			if (e.getType().equals("Restricted Holiday")) {
				calenderDTO.setDisplay("block");
				calenderDTO.setBackgroundColor("red");
				calenderDTO.setBorderColor("red");
				calenderDTOs.add(calenderDTO);
			} else {
				calenderDTO.setDisplay("block");
				calenderDTO.setBackgroundColor("green");
				calenderDTO.setBorderColor("green");
				calenderDTOs.add(calenderDTO);
			}

		});
		if (employeeId != null) {
			List<Leave> leaves = leaveApplyRepository.findByEmployeeId(employeeId);
			leaves.forEach(e -> {
				HolidayCalenderDTO calenderDTO = new HolidayCalenderDTO();
				calenderDTO.setId(e.getId());
				calenderDTO.setStart(e.getStartDate());
				calenderDTO.setEnd(e.getEndDate());
				long leaveType = e.getLeaveTypeId();
				LeaveType leaveType2 = leaveTypeRepository.findById(leaveType).get();
				String string = leaveType2.getLeaveType();

				if (e.getLeaveStatusId() == 1) {

					if (string.equalsIgnoreCase("Earn Leave")) {
						calenderDTO.setTitle(leaveType2.getLeaveType());
						calenderDTO.setDisplay("block");
						calenderDTO.setBackgroundColor("blue");
						calenderDTO.setBorderColor("blue");
						calenderDTOs.add(calenderDTO);

					} else if (string.equalsIgnoreCase("Casual Leave")) {
						calenderDTO.setTitle(leaveType2.getLeaveType());
						calenderDTO.setDisplay("block");
						calenderDTO.setBackgroundColor("yellow");
						calenderDTO.setBorderColor("yellow");
						calenderDTOs.add(calenderDTO);
					}

					else if (string.equalsIgnoreCase("Sick Leave")) {
						calenderDTO.setTitle(leaveType2.getLeaveType());
						calenderDTO.setDisplay("block");
						calenderDTO.setBackgroundColor("black");
						calenderDTO.setBorderColor("black");
						calenderDTOs.add(calenderDTO);
					}

					else if (string.equalsIgnoreCase("My Leave")) {
						calenderDTO.setTitle(leaveType2.getLeaveType());
						calenderDTO.setDisplay("block");
						calenderDTO.setBackgroundColor("pink");
						calenderDTO.setBorderColor("pink");
						calenderDTOs.add(calenderDTO);

					} else if (string.equalsIgnoreCase("Company Holiday")) {

						calenderDTO.setTitle(leaveType2.getLeaveType());
						calenderDTO.setDisplay("block");
						calenderDTO.setBackgroundColor("brown");
						calenderDTO.setBorderColor("brown");
						calenderDTOs.add(calenderDTO);
					}

				}
			});

		} else

		{
			List<Leave> leaves = leaveApplyRepository.findByEmployeeId(jwtTokenUtil.getToken(httpServletRequest));
			leaves.forEach(e -> {
				HolidayCalenderDTO calenderDTO = new HolidayCalenderDTO();
				calenderDTO.setId(e.getId());
				calenderDTO.setStart(e.getStartDate());
				calenderDTO.setEnd(e.getEndDate());
				long leaveType = e.getLeaveTypeId();
				LeaveType leaveType2 = leaveTypeRepository.findById(leaveType).get();
				String string = leaveType2.getLeaveType();

				if (e.getLeaveStatusId() == 1) {

					if (string.equalsIgnoreCase("Earn Leave")) {

						calenderDTO.setTitle(leaveType2.getLeaveType());
						calenderDTO.setDisplay("block");
						calenderDTO.setBackgroundColor("blue");
						calenderDTO.setBorderColor("blue");
						calenderDTOs.add(calenderDTO);

					} else if (string.equalsIgnoreCase("Casual Leave")) {
						calenderDTO.setTitle(leaveType2.getLeaveType());
						calenderDTO.setDisplay("block");
						calenderDTO.setBackgroundColor("yellow");
						calenderDTO.setBorderColor("yellow");
						calenderDTOs.add(calenderDTO);
					}

					if (string.equalsIgnoreCase("Restricted Holiday")) {
						calenderDTO.setTitle(leaveType2.getLeaveType());
						calenderDTO.setDisplay("block");
						calenderDTO.setBackgroundColor("blue");
						calenderDTO.setBorderColor("blue");
						calenderDTOs.add(calenderDTO);
					} else if (string.equalsIgnoreCase("Sick Leave")) {
						calenderDTO.setTitle(leaveType2.getLeaveType());
						calenderDTO.setDisplay("block");
						calenderDTO.setBackgroundColor("black");
						calenderDTO.setBorderColor("black");
						calenderDTOs.add(calenderDTO);
					}

					else if (string.equalsIgnoreCase("My Leave")) {
						calenderDTO.setTitle(leaveType2.getLeaveType());
						calenderDTO.setDisplay("block");
						calenderDTO.setBackgroundColor("pink");
						calenderDTO.setBorderColor("pink");
						calenderDTOs.add(calenderDTO);

					} else if (string.equalsIgnoreCase("Company Holiday")) {

						calenderDTO.setTitle(leaveType2.getLeaveType());
						calenderDTO.setDisplay("block");
						calenderDTO.setBackgroundColor("brown");
						calenderDTO.setBorderColor("brown");
						calenderDTOs.add(calenderDTO);
					}
				}
			});
		}
		return calenderDTOs;
	}

	public List<LeavePendingDTO> getPendingLeave(HttpServletRequest httpServletRequest) {
		long id = 2;
		resPendingList.clear();
		List<Leave> pendingList = leaveApplyRepository
				.findByEmployeeIdAndStatusId(jwtTokenUtil.getToken(httpServletRequest), id);
		Employee emp = employeeRepository.findByEmployeeId(jwtTokenUtil.getToken(httpServletRequest));
		pendingList.forEach(e -> {
			LeavePendingDTO leavePendingDto = new LeavePendingDTO();
			leavePendingDto.setId(e.getId());
			leavePendingDto.setEmployeeId(e.getEmployeeId());
			leavePendingDto.setEmployeeName(emp.getName());
			leavePendingDto.setStartDate(e.getStartDate());
			leavePendingDto.setEndDate(e.getEndDate());
			leavePendingDto.setReason(e.getReason());
			leavePendingDto.setStatus("Pending");
			long leaveTypeId = e.getLeaveTypeId();
			LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId).get();
			leavePendingDto.setLeaveType(leaveType.getLeaveType());
			long managerId = e.getManagerId();
			Employee manager = employeeRepository.findByEmployeeId(managerId);
			leavePendingDto.setManagerName(manager.getName());
			resPendingList.add(leavePendingDto);

		});
		return resPendingList;
	}

	@Override
	public List<EmployeeLeaveInfoDTO> getEmployeeLeaveInfo(long employeeId, String localedate) {

		Employee emp = employeeRepository.findByEmployeeId1(employeeId);

		String fromDate = localedate + " " + "00:00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		LocalDateTime startDateTime = LocalDateTime.parse(fromDate, formatter);
		String startDate = startDateTime.format(formatter);
		List<Leave> list = leaveApplyRepository.findByIdAndDate(employeeId, startDate);

		List<EmployeeLeaveInfoDTO> empList = new ArrayList<>();
		list.forEach(e -> {

			if (e.getLeaveStatusId() == 1) {

				EmployeeLeaveInfoDTO employeeLeaveInfoDTO = new EmployeeLeaveInfoDTO();
				employeeLeaveInfoDTO.setEmployeeId(employeeId);
				String name = emp.getName();
				employeeLeaveInfoDTO.setEmployeeName(name);
				employeeLeaveInfoDTO.setFromDate(e.getStartDate());
				employeeLeaveInfoDTO.setToDate(e.getEndDate());
				employeeLeaveInfoDTO.setReason(e.getReason());
				long id = e.getLeaveTypeId();
				String type = leaveApplyRepository.findLeaveType(id);
				employeeLeaveInfoDTO.setLeaveType(type);
				empList.add(employeeLeaveInfoDTO);
			}
		});

		return empList;
	}

	@Override
	public GrantedLeave getGrantedLeaves(HttpServletRequest httpServletRequest) {
		return grantedLeaveRepository.findByEmployeeId(jwtTokenUtil.getToken(httpServletRequest));
	}

	@Override
	public Leave updateLeaveStatus(HttpServletRequest httpServletRequest, long leaveId, long statusId)
			throws ParseException {
		Leave leaveStatus = leaveApplyRepository.findById(leaveId).get();
		if (statusId == 1) {
			leaveStatus.setLeaveStatusId(statusId);
		}
		if (statusId == 3) {

			leaveStatus.setLeaveStatusId(statusId);
			leaveStatus.setApprovedBy(jwtTokenUtil.getToken(httpServletRequest));

			Optional<LeaveType> leaveType = leaveTypeRepository.findById(leaveStatus.getLeaveTypeId());

			LeaveBalance leaveBalance = leaveRepository.findByEmployeeId(leaveStatus.getEmployeeId());
			LeaveBalance leaveBalanceUpdated = new LeaveBalance();
			String string = leaveType.get().getLeaveType();
			SimpleDateFormat myFormat1 = new SimpleDateFormat("dd-MM-yyyy");
			Date date1 = myFormat1.parse(leaveStatus.getStartDate());
			Date date2 = myFormat1.parse(leaveStatus.getEndDate());
			String stDate = leaveStatus.getStartDate() + " " + "00:00:00";
			String enDate = leaveStatus.getEndDate() + " " + "00:00:00";
			DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			LocalDateTime startDate = LocalDateTime.parse(stDate, myFormat);
			LocalDateTime endDate = LocalDateTime.parse(enDate, myFormat);

			long day = 0;
			LocalDateTime startDateTime = startDate;
			while (startDateTime.compareTo(endDate) <= 0) {
				LocalDateTime nextDate = startDateTime.plusDays(1);

				DayOfWeek expected = startDateTime.getDayOfWeek();
				DayOfWeek day1 = DayOfWeek.SATURDAY;
				DayOfWeek day2 = DayOfWeek.SUNDAY;
				if (expected == day1 || expected == day2) {
					day++;
				}
				startDateTime = nextDate;
			}

			long diff = date2.getTime() - date1.getTime();
			float days = (diff / (1000 * 60 * 60 * 24));
			long aValue = (long) days;
			long leftDay = aValue - day;
			long leftDays = leftDay + 1;
			if (string.equalsIgnoreCase("Casual Leave")) {
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave() + leftDays);
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());

			} else if (string.equalsIgnoreCase("Sick Leave")) {
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave() + leftDays);
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());

			} else if (string.equalsIgnoreCase("My Leave")) {
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave() + leftDays);
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

			} else if (string.equalsIgnoreCase("Restricted Holiday")) {
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday() + leftDays);
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

			} else if (string.equalsIgnoreCase("Company Holiday")) {
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday() + leftDays);
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

			} else if (string.equalsIgnoreCase("Earn Leave")) {
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave() + leftDays);
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

			}

			leaveRepository.updateByEmployeeId(leaveBalanceUpdated.getCasualLeave(), leaveBalanceUpdated.getSickLeave(),
					leaveBalanceUpdated.getEarnedLeave(), leaveBalanceUpdated.getCompanyHoliday(),
					leaveBalanceUpdated.getRestrictedHoliday(), leaveBalanceUpdated.getMyLeave(),
					leaveBalanceUpdated.getEmployeeId());
		}
		if (statusId == 4) {
			leaveStatus.setLeaveStatusId(statusId);
			leaveStatus.setApprovedBy(jwtTokenUtil.getToken(httpServletRequest));

			Optional<LeaveType> leaveType = leaveTypeRepository.findById(leaveStatus.getLeaveTypeId());

			LeaveBalance leaveBalance = leaveRepository.findByEmployeeId(jwtTokenUtil.getToken(httpServletRequest));
			LeaveBalance leaveBalanceUpdated = new LeaveBalance();
			String string = leaveType.get().getLeaveType();
			SimpleDateFormat myFormat1 = new SimpleDateFormat("dd-MM-yyyy");
			Date date1 = myFormat1.parse(leaveStatus.getStartDate());
			Date date2 = myFormat1.parse(leaveStatus.getEndDate());
			String stDate = leaveStatus.getStartDate() + " " + "00:00:00";
			String enDate = leaveStatus.getEndDate() + " " + "00:00:00";
			DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			LocalDateTime startDate = LocalDateTime.parse(stDate, myFormat);
			LocalDateTime endDate = LocalDateTime.parse(enDate, myFormat);

			long day = 0;
			LocalDateTime startDateTime = startDate;
			while (startDateTime.compareTo(endDate) <= 0) {
				LocalDateTime nextDate = startDateTime.plusDays(1);

				DayOfWeek expected = startDateTime.getDayOfWeek();
				DayOfWeek day1 = DayOfWeek.SATURDAY;
				DayOfWeek day2 = DayOfWeek.SUNDAY;
				if (expected == day1 || expected == day2) {
					day++;
				}
				startDateTime = nextDate;
			}

			long diff = date2.getTime() - date1.getTime();
			float days = (diff / (1000 * 60 * 60 * 24));
			long aValue = (long) days;
			long leftDay = aValue - day;
			long leftDays = leftDay + 1;

			if (string.equalsIgnoreCase("Casual Leave")) {
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave() + leftDays);
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());

			} else if (string.equalsIgnoreCase("Sick Leave")) {
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave() + leftDays);
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());

			} else if (string.equalsIgnoreCase("My Leave")) {
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave() + leftDays);
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

			} else if (string.equalsIgnoreCase("Restricted Holiday")) {
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday() + leftDays);
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

			} else if (string.equalsIgnoreCase("Company Holiday")) {
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday() + leftDays);
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave());
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

			} else if (string.equalsIgnoreCase("Earn Leave")) {
				leaveBalanceUpdated.setEarnedLeave(leaveBalance.getEarnedLeave() + leftDays);
				leaveBalanceUpdated.setRestrictedHoliday(leaveBalance.getRestrictedHoliday());
				leaveBalanceUpdated.setMyLeave(leaveBalance.getMyLeave());
				leaveBalanceUpdated.setCasualLeave(leaveBalance.getCasualLeave());
				leaveBalanceUpdated.setCompanyHoliday(leaveBalance.getCompanyHoliday());
				leaveBalanceUpdated.setEmployeeId(leaveBalance.getEmployeeId());
				leaveBalanceUpdated.setSickLeave(leaveBalance.getSickLeave());

			}

			leaveRepository.updateByEmployeeId(leaveBalanceUpdated.getCasualLeave(), leaveBalanceUpdated.getSickLeave(),
					leaveBalanceUpdated.getEarnedLeave(), leaveBalanceUpdated.getCompanyHoliday(),
					leaveBalanceUpdated.getRestrictedHoliday(), leaveBalanceUpdated.getMyLeave(),
					leaveBalanceUpdated.getEmployeeId());
		}
		return leaveApplyRepository.save(leaveStatus);
	}

	@Override
	public List<LeaveReviewDTO> getManagerReview(HttpServletRequest httpServletRequest) {

		List<LeaveReviewDTO> list = new ArrayList<>();
		List<Leave> leaveReview = leaveApplyRepository.findByManagerId(jwtTokenUtil.getToken(httpServletRequest));

		leaveReview.forEach(e -> {
			long statusId = e.getLeaveStatusId();
			if (statusId == 2) {

				LeaveReviewDTO leaveReviewDTO = new LeaveReviewDTO();
				Employee emp = employeeRepository.findByEmployeeId(e.getEmployeeId());

				long empId = e.getEmployeeId();
				leaveReviewDTO.setEmployeeId(empId);

				String name = emp.getName();
				leaveReviewDTO.setEmployeeName(name);

				SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
				String startDate = e.getStartDate();
				leaveReviewDTO.setStartDate(startDate);

				String endDate = e.getEndDate();
				leaveReviewDTO.setEndDate(endDate);

				Date date1 = null;
				Date date2 = null;
				try {
					date1 = myFormat.parse(e.getStartDate());
					date2 = myFormat.parse(e.getEndDate());
				} catch (ParseException e1) {

					e1.printStackTrace();
				}
				long diff = date2.getTime() - date1.getTime();
				float days = (diff / (1000 * 60 * 60 * 24));
				long aValue = (long) days + 1;
				leaveReviewDTO.setTotalDays(aValue);

				long id = e.getLeaveTypeId();
				String type = leaveApplyRepository.findLeaveType(id);
				leaveReviewDTO.setLeaveType(type);

				leaveReviewDTO.setReason(e.getReason());

				long id2 = e.getId();
				leaveReviewDTO.setLeaveId(id2);

				list.add(leaveReviewDTO);
			} else {

			}
		});

		return list;

	}

	@Override

	public List<LeaveAllEmployeeRoot> getAllEmployeeByManagerId(HttpServletRequest httpServletRequest,
			String localDateTime, String localDateTime1) {
		 allEmployeesRoot.clear();
		String dateTime = localDateTime + " " + "00:00:00";
		String dateTime1 = localDateTime1 + " " + "00:00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		
		List<LeaveAndEmployeeData> leaves = employeeRepositoryEntityManager
				.findAllEmpAndLeave(jwtTokenUtil.getToken(httpServletRequest), dateTime, dateTime1);
		
		leaves.forEach(e -> {

			if (e.getLeaveStatusId() != null) {
				ArrayList<LeaveAllEmployee> allEmployees = new ArrayList<>();
				LeaveAllEmployeeRoot leaveAllEmployeeRoot = new LeaveAllEmployeeRoot();
				leaveAllEmployeeRoot.setEmployeeId(e.getEmployeeId());
				leaveAllEmployeeRoot.setName(e.getName());

				String startDate = e.getStartDate() + " " + "00:00:00";
				String lastDate = e.getEndDate() + " " + "00:00:00";

				startDateTime = LocalDateTime.parse(startDate, formatter);
				LocalDateTime endDateTime = LocalDateTime.parse(lastDate, formatter);

				while (startDateTime.compareTo(endDateTime) <= 0) {

					LeaveAllEmployee allEmployee = new LeaveAllEmployee();
					LocalDateTime nextDate = startDateTime.plusDays(1);
					String setDate = startDateTime.format(formatter);
					String[] arr = setDate.split(" ");

					allEmployee.setDate(arr[0]);

					LeaveType leaveType = leaveTypeRepository.findById(e.getLeaveTypeId()).get();
					if (leaveType.getLeaveType().equals("Restricted Holiday")) {
						allEmployee.setLeaveType(leaveType.getLeaveType());
						allEmployee.setBackgroundColor("green");
						allEmployee.setBorderColor("green");
						allEmployee.setDisplay("block");
						Employee employee1 = employeeRepository.findByEmployeeId(e.getManagerId());
						allEmployee.setApproveBy(employee1.getName());
						allEmployees.add(allEmployee);

					} else if (leaveType.getLeaveType().equals("Earn Leave")) {
						allEmployee.setLeaveType(leaveType.getLeaveType());
						allEmployee.setBackgroundColor("blue");
						allEmployee.setBorderColor("blue");
						allEmployee.setDisplay("block");
						Employee employee1 = employeeRepository.findByEmployeeId(e.getManagerId());
						allEmployee.setApproveBy(employee1.getName());
						allEmployees.add(allEmployee);

					} else if (leaveType.getLeaveType().equals("Casual Leave")) {
						allEmployee.setLeaveType(leaveType.getLeaveType());
						allEmployee.setBackgroundColor("yellow");
						allEmployee.setBorderColor("yellow");
						allEmployee.setDisplay("block");
						Employee employee1 = employeeRepository.findByEmployeeId(e.getManagerId());
						allEmployee.setApproveBy(employee1.getName());
						allEmployees.add(allEmployee);

					} else if (leaveType.getLeaveType().equals("Sick Leave")) {
						allEmployee.setLeaveType(leaveType.getLeaveType());
						allEmployee.setBackgroundColor("black");
						allEmployee.setBorderColor("black");
						allEmployee.setDisplay("block");
						Employee employee1 = employeeRepository.findByEmployeeId(e.getManagerId());
						allEmployee.setApproveBy(employee1.getName());
						allEmployees.add(allEmployee);

					} else if (leaveType.getLeaveType().equals("My Leave")) {
						allEmployee.setLeaveType(leaveType.getLeaveType());
						allEmployee.setBackgroundColor("pink");
						allEmployee.setBorderColor("pink");
						allEmployee.setDisplay("block");
						Employee employee1 = employeeRepository.findByEmployeeId(e.getManagerId());
						allEmployee.setApproveBy(employee1.getName());
						allEmployees.add(allEmployee);

					} else if (leaveType.getLeaveType().equals("Casual Holiday")) {
						allEmployee.setLeaveType(leaveType.getLeaveType());
						allEmployee.setBackgroundColor("brown");
						allEmployee.setBorderColor("brown");
						allEmployee.setDisplay("block");
						Employee employee1 = employeeRepository.findByEmployeeId(e.getManagerId());
						allEmployee.setApproveBy(employee1.getName());
						allEmployees.add(allEmployee);

					}
					startDateTime = nextDate;
				}
				leaveAllEmployeeRoot.setLeaveAllEmployee(allEmployees);
				allEmployeesRoot.add(leaveAllEmployeeRoot);

			} else {
				ArrayList<LeaveAllEmployee> allEmployees = new ArrayList<>();
				LeaveAllEmployeeRoot leaveAllEmployeeRoot = new LeaveAllEmployeeRoot();
				leaveAllEmployeeRoot.setEmployeeId(e.getEmployeeId());
				leaveAllEmployeeRoot.setName(e.getName());
				LeaveAllEmployee allEmployee = new LeaveAllEmployee();
				allEmployees.add(allEmployee);
				leaveAllEmployeeRoot.setLeaveAllEmployee(allEmployees);
				allEmployeesRoot.add(leaveAllEmployeeRoot);
			}

		});
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return format(allEmployeesRoot).stream().map(t -> {
			t.setLeaveAllEmployee(addMissingDatesData(t.getLeaveAllEmployee(),
					LocalDate.parse(localDateTime, formatter1), LocalDate.parse(localDateTime1, formatter1)));
			return t;
		}).collect(Collectors.toList());
	}

	public long getRemainingLeaves(HttpServletRequest httpServletRequest, long id) {
		long balance = 0;
		LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeId(jwtTokenUtil.getToken(httpServletRequest));
		if (id == 1) {
			balance = leaveBalance.getCasualLeave();
		} else if (id == 2) {
			balance = leaveBalance.getSickLeave();
		} else if (id == 3) {
			balance = leaveBalance.getMyLeave();
		} else if (id == 4) {
			balance = leaveBalance.getRestrictedHoliday();
		} else if (id == 5) {
			balance = leaveBalance.getEarnedLeave();
		} else {
			balance = leaveBalance.getCompanyHoliday();
		}
		return balance;

	}

	private static List<LeaveAllEmployeeRoot> format(List<LeaveAllEmployeeRoot> list) {
		return list.stream().collect(Collectors.toMap(t -> t.getEmployeeId(), t -> t, (existing, replacement) -> {
			List<LeaveAllEmployee> leaves = null;
			if (existing.getLeaveAllEmployee() == null) {
				leaves = new ArrayList<>();
				leaves.addAll(existing.getLeaveAllEmployee());
				leaves.addAll(replacement.getLeaveAllEmployee());
			} else {
				leaves = existing.getLeaveAllEmployee();
				leaves.addAll(replacement.getLeaveAllEmployee());
			}
			existing.setLeaveAllEmployee(leaves);
			return existing;
		})).values().stream().collect(Collectors.toList());
	}

	public Map<String, List<LeaveAllEmployeeRoot>> getAllLeaveReveiw(HttpServletRequest httpServletRequest,
			String localDateTime, String localDateTime1) {
		Map<String, List<LeaveAllEmployeeRoot>> maps = new HashMap<>();
		List<LeaveAllEmployeeRoot> allEmployeeRoots = new ArrayList<>();
		List<LeaveAllEmployee> allEmployees = new ArrayList<>();
		String dateTime = localDateTime + " " + "00:00:00";
		String dateTime1 = localDateTime1 + " " + "00:00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		List<LeaveAndEmployeeData> leaves = leaveApplyRepository
				.findByManagerId(jwtTokenUtil.getToken(httpServletRequest), dateTime, dateTime1);
		startDateTime = LocalDateTime.parse(dateTime, formatter);
		LocalDateTime endDateTime = LocalDateTime.parse(dateTime1, formatter);
		leaves.forEach(e -> {
			while (startDateTime.compareTo(endDateTime) <= 0) {
				LocalDateTime nextDate = startDateTime.plusDays(1);
				String setDate = startDateTime.format(formatter);
				String[] arr = setDate.split(" ");
				if (arr[0].equals(e.getStartDate())) {
					LeaveAllEmployeeRoot allEmployeeRoot = new LeaveAllEmployeeRoot();
					LeaveAllEmployee allEmployee = new LeaveAllEmployee();
					allEmployeeRoot.setEmployeeId(e.getEmployeeId());
					Employee employee = employeeRepository.findByEmployeeId(e.getEmployeeId());
					allEmployeeRoot.setName(employee.getName());
					LeaveType leaveType = leaveTypeRepository.getById(e.getLeaveTypeId());
					allEmployee.setLeaveType(leaveType.getLeaveType());
					allEmployee.setDate(e.getStartDate());
					Employee employee2 = employeeRepository.findByEmployeeId(e.getEmployeeId());
					allEmployee.setApproveBy(employee2.getName());
					allEmployees.add(allEmployee);
					allEmployeeRoot.setLeaveAllEmployee(allEmployees);
					allEmployeeRoots.add(allEmployeeRoot);
				}
				startDateTime = nextDate;
				maps.put(arr[0], allEmployeeRoots);
			}
		});
		return maps;
	}

	public static List<LeaveAllEmployee> addMissingDatesData(List<LeaveAllEmployee> resData, LocalDate startDate,
			LocalDate endDate) {
		List<LeaveAllEmployee> finalRes = new ArrayList<>();
		getMissingDates(startDate, endDate).forEach(t -> {
			LeaveAllEmployee o = resData.stream().filter(d -> {
				String date1 = d.getDate();
				String date = t.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
				if (date1 != null) {
					return d.getDate().toString().equals(date);
				} else {
					return date.equals(d.getDate());
				}
			}).findAny().orElse(new LeaveAllEmployee());
			o.setDate(t.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
			finalRes.add(o);
		});
		return finalRes;
	}

	public static List<LocalDate> getMissingDates(LocalDate startDate, LocalDate endDate) {
		long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
		return IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween).mapToObj(startDate::plusDays)
				.collect(Collectors.toList());
	}

	public LeaveDTO getLeaveById(Long id) {
		Leave leave = leaveApplyRepository.findById(id).get();
		LeaveDTO leaveDTO = new LeaveDTO();
		leaveDTO.setEmployeeId(leave.getEmployeeId());
		Employee employee = employeeRepository.findByEmployeeId(leave.getEmployeeId());
		leaveDTO.setEmployeeName(employee.getName());
		leaveDTO.setFromDate(leave.getStartDate());
		leaveDTO.setToDate(leave.getEndDate());
		LeaveType leaveType = leaveTypeRepository.findById(leave.getLeaveTypeId()).get();
		leaveDTO.setLeaveType(leaveType.getLeaveType());
		leaveDTO.setReason(leave.getReason());
		return leaveDTO;

	}

}
