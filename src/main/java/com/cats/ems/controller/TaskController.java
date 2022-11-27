package com.cats.ems.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cats.ems.advice.TrackExecutionTime;
import com.cats.ems.dto.TaskDTO;
import com.cats.ems.dto.TaskStatusDTO;
import com.cats.ems.exception.UserNotFoundException;
import com.cats.ems.model.Task;
import com.cats.ems.model.TaskStatus;
import com.cats.ems.serviceImpl.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@CrossOrigin(value = "*")
@SecurityRequirement(name = "cats.emsapi")
@RequestMapping("/task")
public class TaskController {
	@Autowired
	TaskService taskService;

	@PostMapping("/createtask")
	@Operation(summary = "${createtask.message}")
	@TrackExecutionTime
	public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO) {
		return ResponseEntity.ok(taskService.createTask(taskDTO));
	}

	@GetMapping("/getalltask/{pageNumber}/{pageSize}")
	@Operation(summary = "${allinfo.message}")
	@TrackExecutionTime
	public List<TaskStatusDTO> getAllTask(@PathVariable int pageNumber, @PathVariable int pageSize) {
		return taskService.getAllTask(pageNumber, pageSize);
	}

	@GetMapping("/getbytaskid")
	@Operation(summary = "${taskId.message}")
	@TrackExecutionTime
	public ResponseEntity<TaskStatusDTO> getTaskById(@RequestParam("id") long id) throws UserNotFoundException {
		return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
	}

	@PutMapping("/updatebytaskId")
	@Operation(summary = "${updatetaskId.message}")
	@TrackExecutionTime
	public ResponseEntity<Task> updateTask(@RequestBody TaskDTO taskDTO) throws UserNotFoundException {
		return new ResponseEntity<Task>(taskService.updateTask(taskDTO), HttpStatus.OK);
	}

	@DeleteMapping("/deletebyid")
	@Operation(summary = "${deleteId.message}")
	@TrackExecutionTime
	public ResponseEntity<Task> deleteTask(@RequestParam("id") long id) throws UserNotFoundException {
		taskService.deleteTask(id);

		return new ResponseEntity<Task>(HttpStatus.OK);
	}

	@GetMapping("/getalltask")
	@Operation(summary = "${allinfo.message}")
	@TrackExecutionTime
	public List<TaskStatusDTO> getAllTask(HttpServletRequest httpServletRequest) {
		return taskService.getAllTask(httpServletRequest);
	}

	@GetMapping("/getalltaskStatus")
	@Operation(summary = "${getalltaskstatus.message}")
	@TrackExecutionTime
	public ResponseEntity<List<TaskStatus>> getAllTaskStatus() {
		return ResponseEntity.ok(taskService.getAllTaskStatus());
	}
	@GetMapping("/getTaskAssign")
	@Operation(summary = "${getTaskAssign.message}")
	@TrackExecutionTime
	public ResponseEntity< List<TaskStatusDTO>> getTaskAssign(HttpServletRequest httpServletRequest,@RequestHeader(value = "employeeId", required = false) Long employeeId,
			@RequestHeader("start_date") String startDate){		
		return ResponseEntity.ok(taskService.getAssignedTask(httpServletRequest, employeeId,startDate));
		
	}

}
