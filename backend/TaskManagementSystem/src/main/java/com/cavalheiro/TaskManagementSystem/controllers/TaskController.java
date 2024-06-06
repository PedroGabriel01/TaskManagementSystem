package com.cavalheiro.TaskManagementSystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cavalheiro.TaskManagementSystem.entities.Task;
import com.cavalheiro.TaskManagementSystem.exceptions.RegisterNotFound;
import com.cavalheiro.TaskManagementSystem.exceptions.TaskException;
import com.cavalheiro.TaskManagementSystem.services.TaskService;

@RestController
@RequestMapping("/task")
public class TaskController {

	@Autowired
	private TaskService service;
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping()
	public ResponseEntity<List<Task>> getAll() {
		return ResponseEntity.ok(service.getAll());
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("/{id}")
	public ResponseEntity<Task> get(@PathVariable("id") String id) throws RegisterNotFound {
 		return ResponseEntity.ok(service.findById(Integer.parseInt(id)));
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("status/{id}")
	public ResponseEntity<List<Task>> getByStatus(@PathVariable("id") String id) throws RegisterNotFound {
 		return ResponseEntity.ok(service.getByStatus(Integer.parseInt(id)));
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("status/{id}/{search}")
	public ResponseEntity<List<Task>> getByStatus(@PathVariable("id") String id, @PathVariable("search") String search) throws RegisterNotFound {
 		return ResponseEntity.ok(service.getByStatus(Integer.parseInt(id), search));
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@PostMapping()
	public ResponseEntity<Task> save(@RequestBody Task task) throws RegisterNotFound, TaskException {
		return ResponseEntity.ok(service.save(task));
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") String id) throws NumberFormatException, TaskException {
		service.delete(Integer.parseInt(id));
	}
	
	@ExceptionHandler(TaskException.class)
	public ResponseEntity<String> exceptionHandler(TaskException e) {
		e.printStackTrace();
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	@ExceptionHandler(RegisterNotFound.class)
	public ResponseEntity<String> exceptionHandler(RegisterNotFound e) {
		e.printStackTrace();
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<String> exceptionHandler(NumberFormatException e) {
		e.printStackTrace();
		return ResponseEntity.badRequest().body("Number format invalid");
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> genericExceptionHandler(Exception e) {
		e.printStackTrace();
		return ResponseEntity.internalServerError().body("Something unexpected happened");
	}
}
