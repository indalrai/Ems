package com.cats.ems.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.spi.ValidationProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.cats.ems.dto.TaskDTO;
import com.cats.ems.dto.TaskStatusDTO;
import com.cats.ems.exception.UserNotFoundException;
import com.cats.ems.model.Task;
import com.cats.ems.service.TaskServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TaskServiceControllerTest {

	@Mock
	TaskServiceImpl taskServiceImpl;

	@InjectMocks
	TaskController taskController;

	@Autowired
	TaskDTO taskDTO;

	@Test
	public void createTask() {
		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setAssignTo("Ankit");
		taskDTO.setDescription("this is task for test");
		taskDTO.setEffortHours("7");
		taskDTO.setEmployeeId(1);
		taskDTO.setEndDate("09-11-2022T14:44");
		taskDTO.setEstimatedHours("12");
		taskDTO.setId(4);
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
		task.setCreateTaskDate("08-11-2022 21:09:56");
		task.setUpdateTaskDate("08-11-2022 21:09:56");
		task.setTaskStatusId(1);
		ResponseEntity<Task> taskResponseEntity = ResponseEntity.ok(task);
		when(taskServiceImpl.createTask(taskDTO)).thenReturn(task);

		assertEquals(taskResponseEntity, taskController.createTask(taskDTO));
	}
	@Test
	public void updateTask()
	{
		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setAssignTo("Ankit");
		taskDTO.setDescription("this is task for test");
		taskDTO.setEffortHours("7");
		taskDTO.setEmployeeId(1);
		taskDTO.setEndDate("09-11-2022T14:44");
		taskDTO.setEstimatedHours("12");
		taskDTO.setId(4);
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
		task.setCreateTaskDate("08-11-2022 21:09:56");
		task.setUpdateTaskDate("08-11-2022 21:09:56");
		task.setTaskStatusId(1);
		ResponseEntity<Task> taskResponseEntity = ResponseEntity.ok(task);
		
		ResponseEntity<Task>	expectedResult=null;
		
		try {
			when(taskServiceImpl.updateTask(taskDTO)).thenReturn(task);
	      expectedResult=taskController.updateTask(taskDTO);
		} catch (Exception e) {
			
		}
		
		assertEquals(expectedResult,taskResponseEntity);
	}
	@Test
	public void getTaskById()
	{
		
		
		
		TaskStatusDTO taskStatusDTO = new TaskStatusDTO();
		taskStatusDTO.setTaskName("Code review");
		taskStatusDTO.setDescription("TestCase");
		taskStatusDTO.setEffortHours("12");
		taskStatusDTO.setEmployeeId(1);
		taskStatusDTO.setEndDate("12-11-2022");
		taskStatusDTO.setEstimatedHours("9");
		taskStatusDTO.setId(1);
		taskStatusDTO.setStartDate("12-11-2022");
		taskStatusDTO.setAssignTo("Amit kumar");
		taskStatusDTO.setTaskStatusId(123);
		taskStatusDTO.setStatus("initial");
		taskStatusDTO.setCreateTaskDate("12-11-2022");
		taskStatusDTO.setUpdateTaskDate("13-11-2022");

		ResponseEntity<TaskStatusDTO> taskResponseEntity = ResponseEntity.ok(taskStatusDTO);
		
		ResponseEntity<TaskStatusDTO>	expectedResult=null;
		long id=1;
		
		try {
			when(taskServiceImpl.getTaskById(id)).thenReturn(taskStatusDTO);
		expectedResult=taskController.getTaskById(id);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		assertEquals(expectedResult,taskResponseEntity );
	}
}
