package com.cavalheiro.TaskManagementSystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cavalheiro.TaskManagementSystem.entities.TaskStatus;
import com.cavalheiro.TaskManagementSystem.services.TaskStatusService;

@RestController
@RequestMapping("/status")
public class TaskStatusController {
	
	@Autowired
	private TaskStatusService service;
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping()
	public ResponseEntity<List<TaskStatus>> getAll() {
		return ResponseEntity.ok(service.findAll());
	}
}
