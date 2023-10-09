package ru.practicum.ewm.service.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.dto.user.NewUserDto;
import ru.practicum.ewm.service.dto.user.UserDto;
import ru.practicum.ewm.service.service.interfaces.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserDto userDto) {
        log.info("Создание пользователя: {}", userDto);
        return userService.addUser(userDto);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size
    ) {
        log.info("Вызов всех пользователей");
        return userService.getAllUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удаление пользователя: {}", userId);
        userService.deleteUser(userId);
    }

}
