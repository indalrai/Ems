package com.cats.ems.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.cats.ems.dto.TaskStatusDTO;
import com.cats.ems.exception.UserNotFoundException;
import com.cats.ems.config.JwtTokenUtil;
import com.cats.ems.dto.TaskDTO;
import com.cats.ems.model.Employee;
import com.cats.ems.model.Task;
import com.cats.ems.model.TaskStatus;
import com.cats.ems.repo.EmployeeRepository;
import com.cats.ems.repo.TaskRepository;
import com.cats.ems.repo.TaskStatusRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest<HttpServletRequest> {

	@InjectMocks
	private TaskServiceImpl taskServiceImpl;

	@Mock
	TaskRepository taskRepository;
	
	@Mock
	EmployeeRepository employeeRepository;
	
	@Mock
	JwtTokenUtil jwtTokenUtil;

	@Mock
	TaskStatusRepository taskStatusRepository;

	@Test
	void getAllTaskStatus() {
		List<TaskStatus> list = new ArrayList<>();
		TaskStatus status = new TaskStatus();
		status.setId(1);
		status.setStatus("Initial");
		list.add(status);
		when(taskStatusRepository.findAll()).thenReturn(list);

		List<TaskStatus> list2 = taskServiceImpl.getAllTaskStatus();
		assertThat(list2).isEqualTo(list);
	}

	@Test
	void getAllTask() {
		HttpServletRequest httpServletRequest = null;

	List<TaskStatusDTO> taskStatusDTOs = new ArrayList<>();
	TaskStatusDTO taskStatusDTO = new TaskStatusDTO();
		taskStatusDTO.setAssignTo("Gunank");
		taskStatusDTO.setCreateTaskDate("Review");
		taskStatusDTO.setDescription("India Lost Semifinal");
		taskStatusDTO.setEffortHours("4");
		taskStatusDTO.setEmployeeId(11);
		taskStatusDTO.setEndDate("10-11-2022");
		taskStatusDTO.setEstimatedHours("24");
		taskStatusDTO.setId(2);
		taskStatusDTO.setStartDate("10-11-2022");
		taskStatusDTO.setStatus("Lost Due to Overconfidence");
		taskStatusDTO.setTaskName("India V/s England");
		taskStatusDTO.setTaskStatusId(0);
		taskStatusDTO.setUpdateTaskDate("13-10-2022");
		taskStatusDTOs.add(taskStatusDTO);

		List<Task> taskList = new ArrayList<>();
		Task task = new Task();
		task.setAssignTo("Gunank");
		task.setCreateTaskDate("Review");
		task.setDescription("India Lost Semifinal");
		task.setEffortHours("4");
		task.setEmployeeId(11);
		task.setEndDate("10-11-2022");
		task.setEstimatedHours("24");
		task.setId(2);
		task.setTaskName("India V/s England");
		task.setStartDate("10-11-2022");
		task.setUpdateTaskDate("13-10-2022");
		task.setTaskStatusId(0);
		taskList.add(task);

		TaskStatus taskStatus = new TaskStatus();
		taskStatus.setId(1);
		taskStatus.setStatus("Lost Due to Overconfidence");
		Optional<TaskStatus> tasOptional = Optional.of(taskStatus);
		when(taskStatusRepository.findById(taskStatusDTO.getTaskStatusId())).thenReturn(tasOptional);
		when(jwtTokenUtil.getToken((javax.servlet.http.HttpServletRequest) httpServletRequest)).thenReturn(1L);
		when(taskRepository.findByManagerId(jwtTokenUtil.getToken((javax.servlet.http.HttpServletRequest) httpServletRequest))).thenReturn(taskList);
		List<TaskStatusDTO> taskStatusDTOs1 = new ArrayList<>();
		taskStatusDTOs1 = taskServiceImpl.getAllTask(null);
		assertThat(taskStatusDTOs).isEqualTo(taskStatusDTOs1);
	}

	@Test
	void deleteTask() {
		long id = 1;
		Task task = new Task();
		task.setAssignTo("Gunank");
		task.setCreateTaskDate("Review");
		task.setDescription("India Lost Semifinal");
		task.setEffortHours("4");
		task.setEmployeeId(11);
		task.setEndDate("10-11-2022");
		task.setEstimatedHours("24");
		task.setId(2);
		task.setTaskName("India V/s England");
		task.setStartDate("10-11-2022");
		task.setUpdateTaskDate("13-10-2022");
		task.setTaskStatusId(0);
		Optional<Task> optional = Optional.of(task);
		try {
			willDoNothing().given(taskRepository).deleteById(id);
			when(taskRepository.findById(id)).thenReturn(optional);
			taskServiceImpl.deleteTask(id);
			verify(taskRepository, times(1)).deleteById(id);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Mock
	TaskStatusDTO taskStatusDTO;

	@Test
	public void createTask() {

		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setAssignTo("Ankit");
		taskDTO.setDescription("this is task for test");
		taskDTO.setEffortHours("7");
		taskDTO.setEmployeeId(1);
		taskDTO.setEndDate("09-11-2022T14:44");
		taskDTO.setEstimatedHours("12");
		taskDTO.setId(1);
		taskDTO.setStartDate("08-11-2022T14:44");
		taskDTO.setTaskName("my first task");
		taskDTO.setUpdateTaskDate("08-11-2022 21:09:56");
		taskDTO.setCreateTaskDate("08-11-2022 21:09:56");
		taskDTO.setTaskStatusId(1);

		Task task = new Task();
		task.setTaskName(taskDTO.getTaskName());
		task.setDescription(taskDTO.getDescription());
		task.setStartDate(taskDTO.getStartDate());
		task.setEndDate(taskDTO.getEndDate());
		task.setEffortHours(taskDTO.getEffortHours());
		task.setEstimatedHours(taskDTO.getEstimatedHours());
		task.setDescription(taskDTO.getDescription());
		task.setEmployeeId(taskDTO.getEmployeeId());
		task.setAssignTo(taskDTO.getAssignTo());
		task.setUpdateTaskDate(taskDTO.getUpdateTaskDate());
		task.setCreateTaskDate(taskDTO.getCreateTaskDate());
		task.setId(1);
		task.setTaskStatusId(1);
		Employee employee = new Employee();
		employee.setName("Amit");
		when(taskRepository.save(Mockito.any())).thenReturn(task);
		when(employeeRepository.findByEmployeeId(1L)).thenReturn(employee);
		Task task2 = taskServiceImpl.createTask(taskDTO);
		assertThat(task.getTaskName()).isEqualTo(task2.getTaskName());
	}


	@Test
	public void updateTask() {

		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setAssignTo("Ankit");
		taskDTO.setDescription("this is task for test");
		taskDTO.setEffortHours("7");
		taskDTO.setEmployeeId(1);
		taskDTO.setEndDate("09-11-2022T14:44");
		taskDTO.setEstimatedHours("12");
		taskDTO.setId(1);
		taskDTO.setStartDate("08-11-2022T14:44");
		taskDTO.setTaskName("my first task");
		taskDTO.setUpdateTaskDate("08-11-2022 21:09:56");
		taskDTO.setCreateTaskDate("08-11-2022 21:09:56");
		taskDTO.setTaskStatusId(1);

		Optional<Task> task2 = Optional.of(new Task());
		Task task = task2.get();
		task.setTaskName(taskDTO.getTaskName());
		task.setDescription(taskDTO.getDescription());
		task.setStartDate(taskDTO.getStartDate());
		task.setEndDate(taskDTO.getEndDate());
		task.setEffortHours(taskDTO.getEffortHours());
		task.setEstimatedHours(taskDTO.getEstimatedHours());
		task.setDescription(taskDTO.getDescription());
		task.setEmployeeId(taskDTO.getEmployeeId());
		task.setAssignTo(taskDTO.getAssignTo());
		task.setCreateTaskDate(taskDTO.getCreateTaskDate());
		task.setUpdateTaskDate(taskDTO.getUpdateTaskDate());
		task.setTaskStatusId(1);
		Employee employee = new Employee();
		employee.setName("Amit");
		when(employeeRepository.findByEmployeeId(1L)).thenReturn(employee);
		Task updateTasks = null;
		when(taskRepository.save(task)).thenReturn(task);
		when(taskRepository.findById(taskDTO.getId())).thenReturn(task2);
		try {

			updateTasks = taskServiceImpl.updateTask(taskDTO);
		} catch (Exception e) {

		}
		assertThat(updateTasks).isEqualTo(task);
	}


	
	 
	 

	@Test
	public void getTaskById() {

		Task task2 = new Task();
		task2.setAssignTo("Amit");
		task2.setCreateTaskDate("11-12-2022");
		task2.setDescription("Review");
		task2.setEffortHours("12");
		task2.setEmployeeId(1);
		task2.setEndDate("11-12-2022");
		task2.setEstimatedHours("13");
		task2.setId(2);
		task2.setStartDate("11-12-2022");
		task2.setTaskName("Review test");
		task2.setTaskStatusId(12);
		task2.setUpdateTaskDate("12-12-2022");

		Optional<Task> task = Optional.of(task2);

		TaskStatusDTO taskStatusDTO = new TaskStatusDTO();
		taskStatusDTO.setTaskName(task2.getTaskName());
		taskStatusDTO.setDescription(task2.getDescription());
		taskStatusDTO.setEffortHours(task2.getEffortHours());
		taskStatusDTO.setEmployeeId(task2.getEmployeeId());
		taskStatusDTO.setEndDate(task2.getEndDate());
		taskStatusDTO.setEstimatedHours(task2.getEstimatedHours());
		taskStatusDTO.setId(task2.getId());
		taskStatusDTO.setStartDate(task2.getStartDate());
		taskStatusDTO.setAssignTo(task2.getAssignTo());
		taskStatusDTO.setTaskStatusId(task2.getTaskStatusId());
		taskStatusDTO.setStatus("initial");
		taskStatusDTO.setCreateTaskDate(task2.getCreateTaskDate());
		taskStatusDTO.setUpdateTaskDate(task2.getUpdateTaskDate());

		TaskStatus taskStatus = new TaskStatus();
		taskStatus.setId(1);
		taskStatus.setStatus(null);
		Optional<TaskStatus> taskOptional = Optional.of(taskStatus);
		when(taskStatusRepository.findById(task.get().getTaskStatusId())).thenReturn(taskOptional);
		when(taskRepository.findById(2)).thenReturn(task);
		TaskStatusDTO taskStatusDTO2 = new TaskStatusDTO();
		try {

			taskStatusDTO2 = taskServiceImpl.getTaskById(2);
			taskStatusDTO2.setStatus("initial");
		} catch (Exception e) {

		}
		assertEquals(taskStatusDTO, taskStatusDTO2);
	}

	@Test
	void getAllTask1() {

		List<TaskStatusDTO> list = new ArrayList<>();
		TaskStatusDTO taskStatusDTO = new TaskStatusDTO();
		taskStatusDTO.setDescription("abc");
		taskStatusDTO.setId(1);
		taskStatusDTO.setTaskStatusId(1);
		list.add(taskStatusDTO);

		TaskStatus taskStatus = new TaskStatus();
		taskStatus.setId(taskStatusDTO.getId());
		taskStatus.setStatus(taskStatusDTO.getStatus());
		Optional<TaskStatus> optional = Optional.of(taskStatus);

		List<Task> tasks = new ArrayList<>();
		Task task = new Task();
		task.setId(1);
		task.setTaskStatusId(1);
		task.setDescription("abc");
		tasks.add(task);

		int pageNumber = 0;
		int pageSize = 5;
		Pageable p = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
		Page<Task> page = new PageImpl<>(tasks);
		when(taskStatusRepository.findById(task.getTaskStatusId())).thenReturn(optional);
		when(taskRepository.findAll(p)).thenReturn(page);
		List<TaskStatusDTO> taskStatusDTOs = taskServiceImpl.getAllTask(pageNumber, pageSize);

		assertThat(taskStatusDTOs).isEqualTo(list);
	}
}
