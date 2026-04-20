package com.example.University.mapper;

import com.example.University.dto.RegistrationResponseDTO;
import com.example.University.model.Registration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "workshop.id", target = "workshopId")
    @Mapping(source = "workshop.title", target = "workshopTitle")
    @Mapping(source = "workshop.startDatetime", target = "workshopStartDatetime")
    RegistrationResponseDTO toResponseDTO(Registration registration);
}
