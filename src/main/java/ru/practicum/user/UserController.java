// user/UserController.java (Corrected Injection)
package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.validation.ValidationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    // Remove ValidationService from here
    // private final ValidationService validationService;


    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        log.info("Received POST request to create User: {}", userDto);
        User user = userMapper.toUser(userDto);
        // Moved validation to service layer
        // validationService.validateUserFields(user);
        // validationService.checkUniqueEmailToCreate(user);
        return  userMapper.toUserDto(userService.saveUser(user));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Received PATCH request to update user ID {}: {}", userId, userDto);

        // Removed validation from controller
        // User user = userMapper.toUser(userDto);
        // validationService.checkExistUserInDB(userId);
        // validationService.checkUniqueEmailToUpdate(user);
        return userMapper.toUserDto(userService.updateUser(userMapper.toUser(userDto), userId)); // Pass user and userId
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable Long userId) {
        log.info("Received GET request to retrieve user ID {}", userId);
        return userMapper.toUserDto(userService.getUserById(userId));
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Received GET request to retrieve all users");
        return userService.getAllUsers().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Received DELETE request to delete user ID {}", userId);
        userService.deleteUser(userId);
    }
}