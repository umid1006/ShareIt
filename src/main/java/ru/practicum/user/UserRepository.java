// UserRepository.java
package ru.practicum.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import Query
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long ownerId);
    Optional<User> findByEmail(String email); // Add this method

    // Keep this as an example of a custom query, but use findByEmail
    @Query("select u.id from User u where u.email = :email")
    Long getUserIdByEmail(@Param("email")String userEmail);

    @Query("select u from User u where u.id = :id")
    User getUserById(@Param("id") Long id);
}