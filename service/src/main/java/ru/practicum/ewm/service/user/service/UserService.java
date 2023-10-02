package ru.practicum.ewm.service.user.service;

import ru.practicum.ewm.service.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    void deleteUser(Long id);

    List<UserDto> getUsers(List<Long> ids, int from, int size);
}
