package com.example.University.mapper;

import com.example.University.dto.WorkshopResponseDTO;
import com.example.University.model.Workshop;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkshopMapper {
    WorkshopResponseDTO toResponseDTO(Workshop workshop);
}
