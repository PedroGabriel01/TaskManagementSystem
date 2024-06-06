package com.cavalheiro.TaskManagementSystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cavalheiro.TaskManagementSystem.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Integer>{
	List<Task> findByTaskStatusStatusId(Integer statusId);
	
	@Query("SELECT t FROM Task t WHERE (LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND t.taskStatus.statusId = :statusId")
    List<Task> findByTitleOrDescriptionAndStatusId(@Param("searchTerm") String searchTerm, @Param("statusId") Integer statusId);
}
