package ru.practicum.ewm.service.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.user.dto.UserDto;
import ru.practicum.ewm.service.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping()
    public List<UserDto> get(@RequestParam(required = false) List<Long> ids,
                             @Valid @RequestParam(defaultValue = "0") int from,
                             @Valid @RequestParam(defaultValue = "10") int size) {
        return userService.getUsers(ids, from, size);
    }
}
