package com.cats.ems.serviceImpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cats.ems.dto.TaskDTO;
import com.cats.ems.dto.TaskStatusDTO;
import com.cats.ems.exception.UserNotFoundException;
import com.cats.ems.model.Task;
import com.cats.ems.model.TaskStatus;

public interface TaskService {

	Task createTask(TaskDTO taskDTO);

	Task updateTask(TaskDTO taskDTO) throws UserNotFoundException;

	void deleteTask(long id) throws UserNotFoundException;

	TaskStatusDTO getTaskById(long id) throws UserNotFoundException;

	List<TaskStatus> getAllTaskStatus();

	List<TaskStatusDTO> getAllTask(int pageNumber, int pageSize);
	
	public List<TaskStatusDTO> getAssignedTask(HttpServletRequest httpServletRequest, Long id, String localDateTime);

	List<TaskStatusDTO> getAllTask(HttpServletRequest httpServletRequest);
	
	


	
}
