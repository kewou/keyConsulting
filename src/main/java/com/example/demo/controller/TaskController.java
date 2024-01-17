/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.controller;

import com.example.demo.domain.dto.TaskDto;
import com.example.demo.domain.entities.Task;
import com.example.demo.domain.exception.NotFoundException;
import com.example.demo.domain.mapper.TaskMapper;
import com.example.demo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Joel NOUMIA
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Toutes les taches", description = "Toutes les taches")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Erreur de saisie", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred", content = @Content)

    })
    @GetMapping(path = "")
    public ResponseEntity<List<TaskDto>> getAllTaks() {
        List<TaskDto> listDto = new ArrayList<>();
        taskService.getAllTask().forEach(task -> {
                    TaskDto dto = TaskMapper.getMapper().dto(task);
                    listDto.add(dto);
                }
        );
        return listDto.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(listDto);
    }

    @Operation(summary = "Retourne une tache", description = "Retourne une tache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Erreur de saisie", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred", content = @Content)

    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable("id") long id) throws NotFoundException {
        TaskDto dto = TaskMapper.getMapper().dto(taskService.getTask(id));
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Toutes les taches à effectuer", description = "Toutes les taches à effectuer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Erreur de saisie", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred", content = @Content)

    })
    @GetMapping(path = "/toDo")
    public ResponseEntity<List<TaskDto>> getToDoList() {
        List<TaskDto> listDto = new ArrayList<>();
        taskService.getToDoList().forEach(task -> {
                    TaskDto dto = TaskMapper.getMapper().dto(task);
                    listDto.add(dto);
                }
        );
        return listDto.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(listDto);
    }

    @Operation(summary = "Ajouter une tache", description = "Ajouter une tache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Erreur de saisie", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred", content = @Content)

    })
    @PostMapping(path = "")
    public ResponseEntity<TaskDto> addTask(@RequestBody @Valid TaskDto taskDto) {
        Task task = TaskMapper.getMapper().entitie(taskDto);
        taskService.addTask(task);
        URI uri = URI.create("/tasks/" + task.getId());
        return ResponseEntity.created(uri).body(taskDto);
    }

    @Operation(summary = "Modifier le statut d'une tache", description = "Modifier le statut d'une tache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Erreur de saisie", content = @Content),
            @ApiResponse(responseCode = "500", description = "An Internal Server Error occurred", content = @Content)

    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<TaskDto> updateStatus(@PathVariable("id") long id) throws NotFoundException {
        TaskDto taskDto = TaskMapper.getMapper().dto(taskService.updateStatus(id));
        return ResponseEntity.ok(taskDto);
    }

}
