/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service;


import com.example.demo.domain.entities.Task;
import com.example.demo.domain.exception.NotFoundException;
import com.example.demo.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JOEL NOUMIA
 */
@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task getTask(Long id) throws NotFoundException {
        Task task = getTaskById(id);
        logger.info("Task " + task.getLabel() + " is found");
        return task;
    }


    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }


    public List<Task> getToDoList() {
        return taskRepository.findToDoTask();
    }


    public Task addTask(Task task) {
        taskRepository.save(task);
        logger.info("Task " + task.getLabel() + " has been added");
        return task;
    }

    public Task updateStatus(Long idTask) throws NotFoundException {
        Task task = getTaskById(idTask);
        logger.info("Task " + task.getLabel() + " is found");
        task.setCompleted(!task.isCompleted());
        taskRepository.save(task);
        logger.info("Task " + task.getLabel() + " has been updated");
        return task;
    }

    private Task getTaskById(Long id) throws NotFoundException {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task id " + id + " -> Not Found"));
    }

    private Logger logger = LoggerFactory.getLogger(TaskService.class);


}
