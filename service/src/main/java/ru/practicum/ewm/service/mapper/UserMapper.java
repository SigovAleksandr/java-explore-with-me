package ru.practicum.ewm.service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.service.dto.user.NewUserDto;
import ru.practicum.ewm.service.dto.user.UserDto;
import ru.practicum.ewm.service.dto.user.UserShortDto;
import ru.practicum.ewm.service.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserShortDto toShortDto(User user);

    UserDto toDto(User user);

    User toModel(NewUserDto newUserRequestDto);
}
