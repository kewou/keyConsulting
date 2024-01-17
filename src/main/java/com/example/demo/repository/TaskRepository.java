package com.example.demo.repository;


import com.example.demo.domain.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Joel NOUMIA
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    @Query("from Task t where t.isCompleted=false")
    public List<Task> findToDoTask();
}