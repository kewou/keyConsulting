package com.example.demo;

import com.example.demo.domain.entities.Task;
import com.example.demo.repository.TaskRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskControllerIntegrationTests {

    @Autowired
    public MockMvc mockMvc;

    private static final String URL = "/tasks";

    @Autowired
    private TaskRepository taskRepository;


    @BeforeEach
    public void clear() {
        taskRepository.deleteAll();
    }

    @Test
    @Transactional
    @Rollback
    public void getAllTaskTest() throws Exception {
        mockMvc.perform(get(URL).
                        contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    @Rollback
    public void createUserTest() throws Exception {
        Assertions.assertTrue(taskRepository.findAll().isEmpty());
        Task newTask = new Task("newTask");
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newTask)))
                .andExpect(status().isCreated());
        Assertions.assertEquals(taskRepository.findAll().size(), 1);
    }

    @Test
    @Transactional
    @Rollback
    public void getTaskTest() throws Exception {
        Assertions.assertTrue(taskRepository.findAll().isEmpty());
        Task newTask = new Task("newTask");
        newTask.setId(1L);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newTask)))
                .andExpect(status().isCreated());
        Assertions.assertEquals(taskRepository.findAll().size(), 1);
        Long id = taskRepository.findAll().get(0).getId();
        String res = this.mockMvc.perform(get(URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();
        JsonNode resNode = asJsonNode(res);
        String label = resNode.get("label").asText();
        Assertions.assertEquals(label, "newTask");
    }

    @Test
    @Transactional
    @Rollback
    public void getToDoTaskTest() throws Exception {
        Assertions.assertTrue(taskRepository.findAll().isEmpty());
        Task newTask = new Task("newTask");
        newTask.setId(1L);
        Task anotherTask = new Task("anotherTask");
        newTask.setId(2L);
        anotherTask.setCompleted(true);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newTask)))
                .andExpect(status().isCreated());
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(anotherTask)))
                .andExpect(status().isCreated());
        Assertions.assertEquals(taskRepository.findAll().size(), 2);
        String res = this.mockMvc.perform(get(URL + "/toDo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();
        JsonNode resNode = asJsonNode(res);
        Assertions.assertEquals(resNode.size(), 1);
        String label = resNode.get(0).get("label").asText();
        Assertions.assertEquals(label, "newTask"); // Seule la tache effectuée est renvoyée
    }

    @Test
    @Transactional
    @Rollback
    public void updateTaskTest() throws Exception {
        Assertions.assertTrue(taskRepository.findAll().isEmpty());
        Task newTask = new Task("newTask");
        newTask.setId(1L);
        Assertions.assertEquals(newTask.isCompleted(), false);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newTask)))
                .andExpect(status().isCreated());
        Assertions.assertEquals(taskRepository.findAll().size(), 1);
        Long id = taskRepository.findAll().get(0).getId();
        this.mockMvc.perform(put(URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()));
        String res = this.mockMvc.perform(get(URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();
        JsonNode resNode = asJsonNode(res);
        String completed = resNode.get("completed").asText();
        Assertions.assertEquals(completed, "true");  // La tache qui était false est passée à true
    }


    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonNode asJsonNode(String objectString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(objectString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
