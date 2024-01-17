package com.example.demo.domain.dto;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Joel NOUMIA
 */

public class TaskDto {

    @NotBlank(message = "Entrez le label de la tache")
    private String label;

    private boolean isCompleted = false;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public TaskDto(String label) {
        this.label = label;
    }

    public TaskDto() {
    }
}
