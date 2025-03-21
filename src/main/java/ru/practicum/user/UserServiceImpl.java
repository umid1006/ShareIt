//user/UserServiceImpl.java (Corrected with Validation and Transactions)
package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import
import ru.practicum.exception.NotFoundException;
import ru.practicum.validation.ValidationService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Add for read-only methods
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ValidationService validationService; // Inject

    @Override
    @Transactional // Add for write operations
    public User saveUser(User user) {
        validationService.validateUserFields(user); // Validate
        validationService.checkUniqueEmailToCreate(user); // Validate
        user.setState(UserState.ACTIVE); // Set initial state
        return userRepository.save(user);
    }

    @Override
    @Transactional // Add for write operations
    public User updateUser(User user, Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        // Validate before updating.  Important!
        validationService.checkExistUserInDB(userId);
        validationService.checkUniqueEmailToUpdate(user);
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getState() != null) { //Update state
            existingUser.setState(user.getState());
        }

        return userRepository.save(existingUser);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new NotFoundException("User with id " + id + " not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional // Add for write operations
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)){
            throw new NotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }
}