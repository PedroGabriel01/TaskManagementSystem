package com.cavalheiro.TaskManagementSystem.services;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cavalheiro.TaskManagementSystem.entities.Task;
import com.cavalheiro.TaskManagementSystem.entities.TaskStatus;
import com.cavalheiro.TaskManagementSystem.exceptions.RegisterNotFound;
import com.cavalheiro.TaskManagementSystem.exceptions.TaskException;
import com.cavalheiro.TaskManagementSystem.repositories.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository repository;

	@Autowired
	private TaskStatusService taskStatusService;

	public List<Task> getAll() {
		return repository.findAll();
	}

	public Task findById(Integer id) throws RegisterNotFound {
		return repository.findById(id).orElseThrow(() -> new RegisterNotFound("Task not found with id:" + id));
	}
	
	public List<Task> getByStatus(Integer id) {
		return repository.findByTaskStatusStatusId(id);
	}
	
	public List<Task> getByStatus(Integer id, String search) {
		return repository.findByTitleOrDescriptionAndStatusId(search, id);
	}

	public Task save(Task task) throws RegisterNotFound, TaskException {
		if (task.getDescription() == null || task.getDescription().isBlank() || task.getTitle() == null || task.getTitle().isBlank()) {
			throw new TaskException("Title and Description needs to be filled");
		}
		
		if (task.getTaskId() == null) {
			return saveNewTask(task);
		} else {
			return updateTask(task);
		}
	}
	
	private Task saveNewTask(Task task) throws TaskException, RegisterNotFound {
		task.setDtCreated(LocalDateTime.now());
		if (!isDayOfWeek(task.getDtCreated())) {
			throw new TaskException("Tasks can't be create in week ends");
		}
		
		TaskStatus taskStatus = taskStatusService.findById(1);
		task.setTaskStatus(taskStatus);
		
		return repository.save(task);
	}
	
	private Task updateTask(Task task) throws RegisterNotFound, TaskException {
		Task oldTask = findById(task.getTaskId());
		TaskStatus oldStatus = taskStatusService.findById(oldTask.getTaskStatus().getStatusId());
		
		if (!oldStatus.isUpdatable() && (!oldTask.getTitle().equals(task.getTitle()) || !oldTask.getDescription().equals(task.getDescription()))) {
			throw new TaskException("Task can't have title or description changed on status " + oldStatus.getDescription());
		}
		
		TaskStatus taskStatus = taskStatusService.findById(task.getTaskStatus().getStatusId());
		task.setTaskStatus(taskStatus);
		task.setDtUpdated(LocalDateTime.now());
		return repository.save(task);
	}

	public void delete(Integer id) throws TaskException {
		try {
			Task task = findById(id);
			if (!task.getTaskStatus().isDeletable()) {
				throw new TaskException("Task on status " + task.getTaskStatus().getDescription() + " can't be deleted");
			}
			LocalDateTime fiveDaysAfterCreation = task.getDtCreated().plusDays(5);
			LocalDateTime now = LocalDateTime.now();
			if (fiveDaysAfterCreation.isAfter(now)) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:ss");
				throw new TaskException("Task can be delete at " + fiveDaysAfterCreation.format(formatter));
			}
			repository.deleteById(id);
		} catch (RegisterNotFound e) {}
	}
	
	public boolean isDayOfWeek(LocalDateTime date) {
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
	}
}
