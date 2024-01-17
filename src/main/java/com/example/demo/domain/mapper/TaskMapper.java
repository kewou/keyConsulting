package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.TaskDto;
import com.example.demo.domain.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * @author Joel NOUMIA
 */

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {

    public static TaskMapper getMapper() {
        return Mappers.getMapper(TaskMapper.class);
    }

    TaskDto dto(Task Task);

    Task entitie(TaskDto TaskDto);
}
