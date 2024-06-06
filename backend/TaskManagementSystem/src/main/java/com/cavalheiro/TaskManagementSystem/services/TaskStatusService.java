package com.cavalheiro.TaskManagementSystem.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cavalheiro.TaskManagementSystem.entities.TaskStatus;
import com.cavalheiro.TaskManagementSystem.exceptions.RegisterNotFound;
import com.cavalheiro.TaskManagementSystem.repositories.TaskStatusRepository;

@Service
public class TaskStatusService {
	
	@Autowired
	private TaskStatusRepository repository;
	
	public List<TaskStatus> findAll() {
		return repository.findAll();
	}
	
	public TaskStatus findById(Integer id) throws RegisterNotFound {
		return repository.findById(id).orElseThrow(() -> new RegisterNotFound("Status not found with id:" + id));
	}
}
