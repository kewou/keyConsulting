package com.example.demo;

import com.example.demo.controller.TaskController;
import com.example.demo.domain.entities.Task;
import com.example.demo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    public void getAllTasksTest() throws Exception {
        Task task1 = new Task("task1");
        List<Task> tasks = Arrays.asList(task1);
        given(taskService.getAllTask()).willReturn(tasks);

        mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))  // Vérifie qu'il ya qu'une tache dans l'objet json
                .andExpect(jsonPath("$[0].label", is(task1.getLabel())))
                .andDo(print());
    }

    @Test
    public void getTaskToDo() throws Exception {
        given(taskService.getAllTask()).willReturn(Collections.emptyList());
        mockMvc.perform(get("/tasks/toDo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()) // Test sur le cas d'une liste vide
                .andDo(print());
    }

    @Test
    public void addTaskTest() throws Exception {
        Task newTask = new Task("newTask");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newTask))) // Convertit l'objet en JSON
                .andExpect(status().isCreated())
                .andDo(print());
        verify(taskService, times(1)).addTask(any(Task.class));  // Vérifie que la methode addTask du service s'est fait correctement
    }

    @Test
    public void getOneTaskTest() throws Exception {
        Task task = new Task("task1");
        given(taskService.getTask(1L)).willReturn(task);

        mockMvc.perform(get("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label", is(task.getLabel())));
    }

    @Test
    public void updateTaskTest() throws Exception {
        Task existingTask = new Task("existingTask");
        given(taskService.getTask(1L)).willReturn(existingTask);

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(taskService, times(1)).updateStatus(1L);
    }


    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
