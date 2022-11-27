package com.cats.ems.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cats.ems.config.JwtTokenUtil;
import com.cats.ems.dto.TaskStatusDTO;
import com.cats.ems.exception.UserNotFoundException;
import com.cats.ems.model.TaskStatus;
import com.cats.ems.serviceImpl.TaskService;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {
	
	@Mock
	TaskService taskService;
	
	@InjectMocks
	TaskController taskController;
	
	@Mock
	JwtTokenUtil jwtTokenUtil;
	

	@Test
	void getAllTaskStatus()
	{
		List<TaskStatus> list=new ArrayList<>();
		TaskStatus status = new TaskStatus();
		status.setId(1);
		status.setStatus("Initial");
		list.add(status);
		ResponseEntity<List<TaskStatus>> expectedResult = ResponseEntity.ok(list);
		when(taskService.getAllTaskStatus()).thenReturn(list);
		
		ResponseEntity<List<TaskStatus>> list2=taskController.getAllTaskStatus();
		assertThat(list2).isEqualTo(expectedResult);
	}
//	@Test
//	void getAllTask()
//	{
//		HttpServletRequest httpServletRequest=null;
//		List<TaskStatusDTO> list = new ArrayList<>();
//		TaskStatusDTO taskStatusDTO = new TaskStatusDTO();
//		taskStatusDTO.setTaskName("Create Api");
//		taskStatusDTO.setDescription("Code");
//		taskStatusDTO.setEffortHours("12");
//		taskStatusDTO.setEmployeeId(1);
//		taskStatusDTO.setEndDate("12-11-2022");
//		taskStatusDTO.setEstimatedHours("23");
//		taskStatusDTO.setId(1);
//		taskStatusDTO.setStartDate("12-11-2022");
//		taskStatusDTO.setAssignTo("Gunank");
//		taskStatusDTO.setTaskStatusId(1);
//		taskStatusDTO.setCreateTaskDate("12-11-2022");
//		taskStatusDTO.setUpdateTaskDate("12-11-2022");
//		list.add(taskStatusDTO);
//		
//		when(jwtTokenUtil.getToken(httpServletRequest)).thenReturn(1L);
//		when(taskService.getAllTask(jwtTokenUtil.getToken(httpServletRequest))).thenReturn(list);
//		
//		assertThat(list).isEqualTo(taskController.getAllTask(httpServletRequest));
//	}
	
	@Test
	void deleteTask()
	{
		long id=1;
		try {
			ResponseEntity<HttpStatus> responseEntity=ResponseEntity.ok(HttpStatus.ACCEPTED);
			 willDoNothing().given(taskService).deleteTask(id);
			taskController.deleteTask(id);
			 verify(taskService, times(1)).deleteTask(id);
		} catch (UserNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	
	
}
