// user/UserController.java (Corrected Injection)
package ru.practicum.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.DuplicateEmailException;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto userDto) { // Add @Valid
        log.info("Received POST request to create User: {}", userDto);
        try {
            User user = userMapper.toUser(userDto);
            UserDto createdUserDto = userMapper.toUserDto(userService.saveUser(user));
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
        } catch (DuplicateEmailException e) {
            // Handle duplicate email
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null); // Or return a custom error DTO
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> update(@Valid @RequestBody UserDto userDto, @PathVariable Long userId) { // Add @Valid
        log.info("Received PATCH request to update user ID {}: {}", userId, userDto);
        try {
            User user = userMapper.toUser(userDto);
            UserDto updatedUserDto = userMapper.toUserDto(userService.updateUser(user, userId));
            return ResponseEntity.ok(updatedUserDto);

        }  catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> get(@PathVariable Long userId) {
        log.info("Received GET request to retrieve user ID {}", userId);
        try {
            UserDto userDto = userMapper.toUserDto(userService.getUserById(userId));
            return ResponseEntity.ok(userDto);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        log.info("Received GET request to retrieve all users");
        List<UserDto> userDtos = userService.getAllUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        log.info("Received DELETE request to delete user ID {}", userId);
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build(); // 204 No Content on successful deletion
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
}
}