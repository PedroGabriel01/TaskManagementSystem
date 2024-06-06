package com.cavalheiro.TaskManagementSystem.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer taskId;
	
	@Column(nullable = false, length = 50)
	private String title;
	
	@Column(length = 500)
	private String description;
	
	@Column(nullable = false)
	private LocalDateTime dtCreated;
	
	private LocalDateTime dtUpdated;
	
	@ManyToOne
	@JoinColumn(name = "status_id", nullable = false)
	private TaskStatus taskStatus;

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaskStatus getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}

	public LocalDateTime getDtCreated() {
		return dtCreated;
	}

	public void setDtCreated(LocalDateTime dtCreated) {
		this.dtCreated = dtCreated;
	}

	public LocalDateTime getDtUpdated() {
		return dtUpdated;
	}

	public void setDtUpdated(LocalDateTime dtUpdated) {
		this.dtUpdated = dtUpdated;
	}
}
