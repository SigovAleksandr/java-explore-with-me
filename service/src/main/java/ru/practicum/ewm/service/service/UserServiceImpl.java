package ru.practicum.ewm.service.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.dto.user.NewUserDto;
import ru.practicum.ewm.service.dto.user.UserDto;
import ru.practicum.ewm.service.mapper.UserMapper;
import ru.practicum.ewm.service.model.User;
import ru.practicum.ewm.service.repository.UserRepository;
import ru.practicum.ewm.service.service.interfaces.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.util.UtilityClass.USER_NOT_FOUND;

@AllArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto addUser(NewUserDto newUserRequestDto) {
        User user = UserMapper.INSTANCE.toModel(newUserRequestDto);
        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        } else {
            userRepository.deleteById(userId);
        }
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers(List<Long> userIds, Integer from, Integer size) {
        List<User> users;
        if (userIds == null || userIds.isEmpty()) {
            PageRequest pageRequest = PageRequest.of(from / size, size);
            users = userRepository.findAll(pageRequest).getContent();
        } else {
            users = userRepository.findAllById(userIds);
        }
        return users.stream()
                .map(UserMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }
}
