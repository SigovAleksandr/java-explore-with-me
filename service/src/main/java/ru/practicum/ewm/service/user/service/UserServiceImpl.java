package ru.practicum.ewm.service.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.exception.ResourceNotFoundException;
import ru.practicum.ewm.service.user.dto.UserDto;
import ru.practicum.ewm.service.user.dto.UserMapper;
import ru.practicum.ewm.service.user.entity.User;
import ru.practicum.ewm.service.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<User> page;
        if (ids != null && !ids.isEmpty()) {
            page = userRepository.findAllByIdIn(ids, pageable);
        } else {
            page = userRepository.findAll(pageable);
        }
        return page.getContent().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
