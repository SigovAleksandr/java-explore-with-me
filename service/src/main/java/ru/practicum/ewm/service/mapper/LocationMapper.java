package ru.practicum.ewm.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.service.dto.location.LocationDto;
import ru.practicum.ewm.service.model.Location;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    Location toModel(LocationDto locationDto);

    LocationDto toDto(Location location);
}