package com.cats.ems.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.cats.ems.config.JwtTokenUtil;
import com.cats.ems.dto.TaskDTO;
import com.cats.ems.dto.TaskStatusDTO;
import com.cats.ems.exception.UserNotFoundException;
import com.cats.ems.model.Employee;
import com.cats.ems.model.Task;
import com.cats.ems.model.TaskStatus;
import com.cats.ems.repo.EmployeeRepository;
import com.cats.ems.repo.TaskRepository;
import com.cats.ems.repo.TaskStatusRepository;
import com.cats.ems.serviceImpl.TaskService;
@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	TaskStatusRepository taskStatusRepository;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Override
	public Task createTask(TaskDTO taskDTO) {
		Task task = new Task();
		task.setTaskName(taskDTO.getTaskName());
		task.setDescription(taskDTO.getDescription());
		task.setStartDate(taskDTO.getStartDate());
		task.setEndDate(taskDTO.getEndDate());
		task.setEffortHours(taskDTO.getEffortHours());
		task.setEstimatedHours(taskDTO.getEstimatedHours());
		task.setEmployeeId(taskDTO.getEmployeeId());
		
		Employee employee = employeeRepository.findByEmployeeId(taskDTO.getEmployeeId());
		task.setAssignTo(employee.getName());
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formatDateTime = now.format(formatter);
		task.setCreateTaskDate(formatDateTime);
		task.setUpdateTaskDate(formatDateTime);
		task.setTaskStatusId(1);
		return taskRepository.save(task);
	}

	@Override
	public Task updateTask(TaskDTO taskDTO) throws UserNotFoundException {

		Task existingTask = taskRepository.findById(taskDTO.getId())
				.orElseThrow(() -> new UserNotFoundException("NOT UPDATED"));
		existingTask.setEndDate(taskDTO.getEndDate());
		existingTask.setEffortHours(taskDTO.getEffortHours());
		existingTask.setEstimatedHours(taskDTO.getEstimatedHours());
		existingTask.setDescription(taskDTO.getDescription());
		existingTask.setStartDate(taskDTO.getStartDate());
		existingTask.setEndDate(taskDTO.getEndDate());
		existingTask.setEmployeeId(taskDTO.getEmployeeId());
		Employee employee = employeeRepository.findByEmployeeId(taskDTO.getEmployeeId());
		existingTask.setAssignTo(employee.getName()); 
		existingTask.setTaskStatusId(taskDTO.getTaskStatusId());
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formatDateTime = now.format(formatter);
		existingTask.setUpdateTaskDate(formatDateTime);
		taskRepository.save(existingTask);
		return existingTask;
	}

	@Override
	public void deleteTask(long id) throws UserNotFoundException {
		taskRepository.findById(id).orElseThrow(() -> new UserNotFoundException("NOT DELETED"));
		taskRepository.deleteById(id);
	}

	@Override
	public List<TaskStatusDTO> getAllTask(int pageNumber, int pageSize) {
		List<TaskStatusDTO> taskStatusDTOs = new ArrayList<>();
		PageRequest p = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
		Page<Task> pageTask = taskRepository.findAll(p);
		pageTask.forEach(e -> {
			TaskStatusDTO taskStatusDTO = new TaskStatusDTO();
			taskStatusDTO.setTaskName(e.getTaskName());
			taskStatusDTO.setDescription(e.getDescription());
			taskStatusDTO.setEffortHours(e.getEffortHours());
			taskStatusDTO.setEmployeeId(e.getEmployeeId());
			taskStatusDTO.setEndDate(e.getEndDate());
			taskStatusDTO.setEstimatedHours(e.getEstimatedHours());
			taskStatusDTO.setId(e.getId());
			taskStatusDTO.setStartDate(e.getStartDate());
			taskStatusDTO.setAssignTo(e.getAssignTo());
			taskStatusDTO.setTaskStatusId(e.getTaskStatusId());
			taskStatusDTO.setCreateTaskDate(e.getCreateTaskDate());
			taskStatusDTO.setUpdateTaskDate(e.getUpdateTaskDate());
			TaskStatus taskStatus = taskStatusRepository.findById(e.getTaskStatusId()).get();
			taskStatusDTO.setStatus(taskStatus.getStatus());
			taskStatusDTOs.add(taskStatusDTO);
		});
		return taskStatusDTOs;
	}

	@Override
	public List<TaskStatus> getAllTaskStatus() {
		return taskStatusRepository.findAll();
	}

	@Override
	public TaskStatusDTO getTaskById(long id) throws UserNotFoundException {
		Task task = taskRepository.findById(id).orElseThrow(() -> new UserNotFoundException("ID NOT FOUND"));
		TaskStatusDTO taskStatusDTO = new TaskStatusDTO();
		taskStatusDTO.setTaskName(task.getTaskName());
		taskStatusDTO.setDescription(task.getDescription());
		taskStatusDTO.setEffortHours(task.getEffortHours());
		taskStatusDTO.setEmployeeId(task.getEmployeeId());
		taskStatusDTO.setEndDate(task.getEndDate());
		taskStatusDTO.setEstimatedHours(task.getEstimatedHours());
		taskStatusDTO.setId(task.getId());
		taskStatusDTO.setStartDate(task.getStartDate());
		taskStatusDTO.setAssignTo(task.getAssignTo());
		taskStatusDTO.setTaskStatusId(task.getTaskStatusId());
		taskStatusDTO.setCreateTaskDate(task.getCreateTaskDate());
		taskStatusDTO.setUpdateTaskDate(task.getUpdateTaskDate());
		TaskStatus taskStatus = taskStatusRepository.findById(task.getTaskStatusId()).get();
		taskStatusDTO.setStatus(taskStatus.getStatus());

		return taskStatusDTO;
	}

	@Override
	public List<TaskStatusDTO> getAllTask(HttpServletRequest httpServletRequest) {
		List<TaskStatusDTO> taskStatusDTOs = new ArrayList<>();
		List<Task> taskList = taskRepository.findByManagerId(jwtTokenUtil.getToken(httpServletRequest));
		taskList.forEach(e -> {
			
			TaskStatusDTO taskStatusDTO = new TaskStatusDTO();
			
			taskStatusDTO.setTaskName(e.getTaskName());
			taskStatusDTO.setDescription(e.getDescription());
			taskStatusDTO.setEffortHours(e.getEffortHours());
			taskStatusDTO.setEmployeeId(e.getEmployeeId());
			taskStatusDTO.setEndDate(e.getEndDate());
			taskStatusDTO.setEstimatedHours(e.getEstimatedHours());
			taskStatusDTO.setId(e.getId());
			taskStatusDTO.setStartDate(e.getStartDate());
			taskStatusDTO.setAssignTo(e.getAssignTo());
			taskStatusDTO.setTaskStatusId(e.getTaskStatusId());
			taskStatusDTO.setCreateTaskDate(e.getCreateTaskDate());
			taskStatusDTO.setUpdateTaskDate(e.getUpdateTaskDate());
			TaskStatus taskStatus = taskStatusRepository.findById(e.getTaskStatusId()).get();
			taskStatusDTO.setStatus(taskStatus.getStatus());
			taskStatusDTOs.add(taskStatusDTO);
		});
		return taskStatusDTOs;
	}

	public List<TaskStatusDTO> getAssignedTask(HttpServletRequest httpServletRequest, Long id,String localDateTime) {
		List<TaskStatusDTO> taskStatusDTOs=new ArrayList<>();
		String dateTime = localDateTime + " " + "00:00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		LocalDateTime startDateTime = LocalDateTime.parse(dateTime, formatter);
		LocalDateTime nextDate = startDateTime.plusDays(1);
		String startDate = startDateTime.format(formatter);
		String endDate = nextDate.format(formatter);
		List<Task> taskList;
		if (id!=null) {
			
			taskList = taskRepository.findByEmployeeId(id, startDate, endDate);
		}else {
			taskList = taskRepository.findByEmployeeId(jwtTokenUtil.getToken(httpServletRequest), startDate, endDate);
		}
			
			taskList.forEach(e -> {
				TaskStatusDTO taskStatusDTO = new TaskStatusDTO();
				taskStatusDTO.setTaskName(e.getTaskName());
				taskStatusDTO.setDescription(e.getDescription());
				taskStatusDTO.setEffortHours(e.getEffortHours());
				taskStatusDTO.setEmployeeId(e.getEmployeeId());
				taskStatusDTO.setEndDate(e.getEndDate());
				taskStatusDTO.setEstimatedHours(e.getEstimatedHours());
				taskStatusDTO.setId(e.getId());
				taskStatusDTO.setStartDate(e.getStartDate());
				taskStatusDTO.setAssignTo(e.getAssignTo());
				taskStatusDTO.setTaskStatusId(e.getTaskStatusId());
				taskStatusDTO.setCreateTaskDate(e.getCreateTaskDate());
				taskStatusDTO.setUpdateTaskDate(e.getUpdateTaskDate());
				TaskStatus taskStatus = taskStatusRepository.findById(e.getTaskStatusId()).get();
				taskStatusDTO.setStatus(taskStatus.getStatus());
				taskStatusDTOs.add(taskStatusDTO);
			});
			return taskStatusDTOs;
	}

}
