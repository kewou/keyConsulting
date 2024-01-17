package com.example.demo;

import com.example.demo.domain.dto.TaskDto;
import com.example.demo.domain.entities.Task;
import com.example.demo.domain.mapper.TaskMapper;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class TaskMapperUnitTests {

    private TaskMapper taskMapper = TaskMapper.getMapper();

    @Test
    public void convert2DtoTest() throws Exception {
        Task task = new Task("Chiffrage");

        TaskDto taskDto = taskMapper.dto(task);

        assertEquals(task.getLabel(), taskDto.getLabel());
        assertEquals(task.isCompleted(), taskDto.isCompleted());
    }

    @Test
    public void convert2EntitieTest() throws Exception {
        TaskDto taskDto = new TaskDto("Test unitaires");

        Task task = taskMapper.entitie(taskDto);
        assertEquals(task.getLabel(), taskDto.getLabel());
        assertEquals(task.isCompleted(), taskDto.isCompleted());
    }


}
