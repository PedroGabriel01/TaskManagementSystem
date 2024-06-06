package com.cavalheiro.TaskManagementSystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cavalheiro.TaskManagementSystem.entities.TaskStatus;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Integer> {

}
