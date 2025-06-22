package com.amarsalimprojects.real_estate_app.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amarsalimprojects.real_estate_app.dto.requests.LoginRequest;
import com.amarsalimprojects.real_estate_app.dto.requests.UserStatistics;
import com.amarsalimprojects.real_estate_app.enums.UserRole;
import com.amarsalimprojects.real_estate_app.model.User;
import com.amarsalimprojects.real_estate_app.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // // CREATE - Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        try {
            User savedUser = userRepository.save(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // CREATE - Create multiple users
    @PostMapping("/bulk")
    public ResponseEntity<List<User>> createUsers(@Valid @RequestBody List<User> users) {
        try {
            List<User> savedUsers = userRepository.saveAll(users);
            return new ResponseEntity<>(savedUsers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get user by username
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
        try {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users by role
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable("role") UserRole role) {
        try {
            List<User> users = userRepository.findByRole(role);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users by first name
    @GetMapping("/first-name/{firstName}")
    public ResponseEntity<List<User>> getUsersByFirstName(@PathVariable("firstName") String firstName) {
        try {
            List<User> users = userRepository.findByFirstName(firstName);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users by last name
    @GetMapping("/last-name/{lastName}")
    public ResponseEntity<List<User>> getUsersByLastName(@PathVariable("lastName") String lastName) {
        try {
            List<User> users = userRepository.findByLastName(lastName);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users by first name and last name
    @GetMapping("/name/{firstName}/{lastName}")
    public ResponseEntity<List<User>> getUsersByFirstNameAndLastName(
            @PathVariable("firstName") String firstName,
            @PathVariable("lastName") String lastName) {
        try {
            List<User> users = userRepository.findByFirstNameAndLastName(firstName, lastName);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Search users by username containing
    @GetMapping("/search/username/{username}")
    public ResponseEntity<List<User>> searchUsersByUsername(@PathVariable("username") String username) {
        try {
            List<User> users = userRepository.findByUsernameContaining(username);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Search users by email containing
    @GetMapping("/search/email/{email}")
    public ResponseEntity<List<User>> searchUsersByEmail(@PathVariable("email") String email) {
        try {
            List<User> users = userRepository.findByEmailContaining(email);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Search users by first name containing
    @GetMapping("/search/first-name/{firstName}")
    public ResponseEntity<List<User>> searchUsersByFirstName(@PathVariable("firstName") String firstName) {
        try {
            List<User> users = userRepository.findByFirstNameContaining(firstName);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Search users by last name containing
    @GetMapping("/search/last-name/{lastName}")
    public ResponseEntity<List<User>> searchUsersByLastName(@PathVariable("lastName") String lastName) {
        try {
            List<User> users = userRepository.findByLastNameContaining(lastName);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users created after date
    @GetMapping("/created-after/{date}")
    public ResponseEntity<List<User>> getUsersCreatedAfter(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            List<User> users = userRepository.findByCreatedAtAfter(date);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users created before date
    @GetMapping("/created-before/{date}")
    public ResponseEntity<List<User>> getUsersCreatedBefore(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            List<User> users = userRepository.findByCreatedAtBefore(date);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users created between dates
    @GetMapping("/created-between")
    public ResponseEntity<List<User>> getUsersCreatedBetween(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<User> users = userRepository.findByCreatedAtBetween(startDate, endDate);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users updated after date
    @GetMapping("/updated-after/{date}")
    public ResponseEntity<List<User>> getUsersUpdatedAfter(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            List<User> users = userRepository.findByUpdatedAtAfter(date);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users updated before date
    @GetMapping("/updated-before/{date}")
    public ResponseEntity<List<User>> getUsersUpdatedBefore(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            List<User> users = userRepository.findByUpdatedAtBefore(date);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users updated between dates
    @GetMapping("/updated-between")
    public ResponseEntity<List<User>> getUsersUpdatedBetween(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<User> users = userRepository.findByUpdatedAtBetween(startDate, endDate);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users by role and created after date
    @GetMapping("/role/{role}/created-after/{date}")
    public ResponseEntity<List<User>> getUsersByRoleAndCreatedAfter(
            @PathVariable("role") UserRole role,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            List<User> users = userRepository.findByRoleAndCreatedAtAfter(role, date);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users by role and updated after date
    @GetMapping("/role/{role}/updated-after/{date}")
    public ResponseEntity<List<User>> getUsersByRoleAndUpdatedAfter(
            @PathVariable("role") UserRole role,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            List<User> users = userRepository.findByRoleAndUpdatedAtAfter(role, date);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users ordered by username
    @GetMapping("/ordered/username")
    public ResponseEntity<List<User>> getUsersOrderedByUsername() {
        try {
            List<User> users = userRepository.findAllByOrderByUsernameAsc();
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users ordered by email
    @GetMapping("/ordered/email")
    public ResponseEntity<List<User>> getUsersOrderedByEmail() {
        try {
            List<User> users = userRepository.findAllByOrderByEmailAsc();
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users ordered by first name
    @GetMapping("/ordered/first-name")
    public ResponseEntity<List<User>> getUsersOrderedByFirstName() {
        try {
            List<User> users = userRepository.findAllByOrderByFirstNameAsc();
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users ordered by last name
    @GetMapping("/ordered/last-name")
    public ResponseEntity<List<User>> getUsersOrderedByLastName() {
        try {
            List<User> users = userRepository.findAllByOrderByLastNameAsc();
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users ordered by created date (newest first)
    @GetMapping("/ordered/created-desc")
    public ResponseEntity<List<User>> getUsersOrderedByCreatedDesc() {
        try {
            List<User> users = userRepository.findAllByOrderByCreatedAtDesc();
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users ordered by created date (oldest first)
    @GetMapping("/ordered/created-asc")
    public ResponseEntity<List<User>> getUsersOrderedByCreatedAsc() {
        try {
            List<User> users = userRepository.findAllByOrderByCreatedAtAsc();
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users ordered by updated date (newest first)
    @GetMapping("/ordered/updated-desc")
    public ResponseEntity<List<User>> getUsersOrderedByUpdatedDesc() {
        try {
            List<User> users = userRepository.findAllByOrderByUpdatedAtDesc();
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users ordered by updated date (oldest first)
    @GetMapping("/ordered/updated-asc")
    public ResponseEntity<List<User>> getUsersOrderedByUpdatedAsc() {
        try {
            List<User> users = userRepository.findAllByOrderByUpdatedAtAsc();
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users by role ordered by username
    @GetMapping("/role/{role}/ordered/username")
    public ResponseEntity<List<User>> getUsersByRoleOrderedByUsername(@PathVariable("role") UserRole role) {
        try {
            List<User> users = userRepository.findByRoleOrderByUsernameAsc(role);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get users by role ordered by created date
    @GetMapping("/role/{role}/ordered/created-desc")
    public ResponseEntity<List<User>> getUsersByRoleOrderedByCreatedDesc(@PathVariable("role") UserRole role) {
        try {
            List<User> users = userRepository.findByRoleOrderByCreatedAtDesc(role);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Check if username exists
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable("username") String username) {
        try {
            boolean exists = userRepository.existsByUsername(username);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Check if email exists
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable("email") String email) {
        try {
            boolean exists = userRepository.existsByEmail(email);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get recent users (last 30 days)
    @GetMapping("/recent")
    public ResponseEntity<List<User>> getRecentUsers() {
        try {
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
            List<User> users = userRepository.findByCreatedAtAfter(thirtyDaysAgo);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get recently updated users (last 7 days)
    @GetMapping("/recently-updated")
    public ResponseEntity<List<User>> getRecentlyUpdatedUsers() {
        try {
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
            List<User> users = userRepository.findByUpdatedAtAfter(sevenDaysAgo);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get admin users
    @GetMapping("/admins")
    public ResponseEntity<List<User>> getAdminUsers() {
        try {
            List<User> users = userRepository.findByRole(UserRole.ADMIN);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get regular users
    @GetMapping("/regular-users")
    public ResponseEntity<List<User>> getRegularUsers() {
        try {
            List<User> users = userRepository.findByRole(UserRole.USER);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // UPDATE - Full update of user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                user.setId(id);
                User updatedUser = userRepository.save(user);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // UPDATE - Partial update of user
    @PatchMapping("/{id}")
    public ResponseEntity<User> partialUpdateUser(@PathVariable("id") Long id, @RequestBody User userUpdates) {
        try {
            Optional<User> existingUserOpt = userRepository.findById(id);
            if (existingUserOpt.isPresent()) {
                User existingUser = existingUserOpt.get();

                // // Update only non-null fields
                if (userUpdates.getUsername() != null) {
                    existingUser.setUsername(userUpdates.getUsername());
                }
                if (userUpdates.getEmail() != null) {
                    existingUser.setEmail(userUpdates.getEmail());
                }
                if (userUpdates.getPassword() != null) {
                    existingUser.setPassword(userUpdates.getPassword());
                }
                if (userUpdates.getRole() != null) {
                    existingUser.setRole(userUpdates.getRole());
                }
                if (userUpdates.getFirstName() != null) {
                    existingUser.setFirstName(userUpdates.getFirstName());
                }
                if (userUpdates.getLastName() != null) {
                    existingUser.setLastName(userUpdates.getLastName());
                }

                User updatedUser = userRepository.save(existingUser);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // UPDATE - Update username
    @PatchMapping("/{id}/username")
    public ResponseEntity<User> updateUsername(@PathVariable("id") Long id, @RequestParam("username") String username) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setUsername(username);
                User updatedUser = userRepository.save(user);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // UPDATE - Update email
    @PatchMapping("/{id}/email")
    public ResponseEntity<User> updateEmail(@PathVariable("id") Long id, @RequestParam("email") String email) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setEmail(email);
                User updatedUser = userRepository.save(user);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // UPDATE - Update password
    @PatchMapping("/{id}/password")
    public ResponseEntity<User> updatePassword(@PathVariable("id") Long id, @RequestParam("password") String password) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setPassword(password);
                User updatedUser = userRepository.save(user);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // UPDATE - Update role
    @PatchMapping("/{id}/role")
    public ResponseEntity<User> updateRole(@PathVariable("id") Long id, @RequestParam("role") UserRole role) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setRole(role);
                User updatedUser = userRepository.save(user);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // UPDATE - Update first name
    @PatchMapping("/{id}/first-name")
    public ResponseEntity<User> updateFirstName(@PathVariable("id") Long id, @RequestParam("firstName") String firstName) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setFirstName(firstName);
                User updatedUser = userRepository.save(user);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // UPDATE - Update last name
    @PatchMapping("/{id}/last-name")
    public ResponseEntity<User> updateLastName(@PathVariable("id") Long id, @RequestParam("lastName") String lastName) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setLastName(lastName);
                User updatedUser = userRepository.save(user);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // UPDATE - Update full name
    @PatchMapping("/{id}/full-name")
    public ResponseEntity<User> updateFullName(
            @PathVariable("id") Long id,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                User updatedUser = userRepository.save(user);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // UPDATE - Promote user to admin
    @PatchMapping("/{id}/promote")
    public ResponseEntity<User> promoteToAdmin(@PathVariable("id") Long id) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setRole(UserRole.ADMIN);
                User updatedUser = userRepository.save(user);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // UPDATE - Demote admin to regular user
    @PatchMapping("/{id}/demote")
    public ResponseEntity<User> demoteToUser(@PathVariable("id") Long id) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                user.setRole(UserRole.USER);
                User updatedUser = userRepository.save(user);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // UPDATE - Bulk update user roles
    @PatchMapping("/bulk-update-role")
    public ResponseEntity<List<User>> bulkUpdateUserRole(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("role") UserRole role) {
        try {
            List<User> updatedUsers = ids.stream()
                    .map(id -> userRepository.findById(id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .peek(user -> user.setRole(role))
                    .map(user -> userRepository.save(user))
                    .toList();

            if (updatedUsers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // UPDATE - Bulk promote users to admin
    @PatchMapping("/bulk-promote")
    public ResponseEntity<List<User>> bulkPromoteToAdmin(@RequestParam("ids") List<Long> ids) {
        try {
            List<User> updatedUsers = ids.stream()
                    .map(id -> userRepository.findById(id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .peek(user -> user.setRole(UserRole.ADMIN))
                    .map(user -> userRepository.save(user))
                    .toList();

            if (updatedUsers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // UPDATE - Bulk demote admins to regular users
    @PatchMapping("/bulk-demote")
    public ResponseEntity<List<User>> bulkDemoteToUser(@RequestParam("ids") List<Long> ids) {
        try {
            List<User> updatedUsers = ids.stream()
                    .map(id -> userRepository.findById(id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .peek(user -> user.setRole(UserRole.USER))
                    .map(user -> userRepository.save(user))
                    .toList();

            if (updatedUsers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // DELETE - Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                userRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // DELETE - Delete all users
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllUsers() {
        try {
            userRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // DELETE - Delete user by username
    @DeleteMapping("/username/{username}")
    public ResponseEntity<HttpStatus> deleteUserByUsername(@PathVariable("username") String username) {
        try {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                userRepository.delete(user.get());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // DELETE - Delete user by email
    @DeleteMapping("/email/{email}")
    public ResponseEntity<HttpStatus> deleteUserByEmail(@PathVariable("email") String email) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                userRepository.delete(user.get());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // DELETE - Delete users by role
    @DeleteMapping("/role/{role}")
    public ResponseEntity<HttpStatus> deleteUsersByRole(@PathVariable("role") UserRole role) {
        try {
            List<User> users = userRepository.findByRole(role);
            if (!users.isEmpty()) {
                userRepository.deleteAll(users);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // DELETE - Delete users by first name
    @DeleteMapping("/first-name/{firstName}")
    public ResponseEntity<HttpStatus> deleteUsersByFirstName(@PathVariable("firstName") String firstName) {
        try {
            List<User> users = userRepository.findByFirstName(firstName);
            if (!users.isEmpty()) {
                userRepository.deleteAll(users);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // DELETE - Delete users by last name
    @DeleteMapping("/last-name/{lastName}")
    public ResponseEntity<HttpStatus> deleteUsersByLastName(@PathVariable("lastName") String lastName) {
        try {
            List<User> users = userRepository.findByLastName(lastName);
            if (!users.isEmpty()) {
                userRepository.deleteAll(users);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // DELETE - Delete users created before date
    @DeleteMapping("/created-before/{date}")
    public ResponseEntity<HttpStatus> deleteUsersCreatedBefore(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            List<User> users = userRepository.findByCreatedAtBefore(date);
            if (!users.isEmpty()) {
                userRepository.deleteAll(users);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // DELETE - Delete users updated before date
    @DeleteMapping("/updated-before/{date}")
    public ResponseEntity<HttpStatus> deleteUsersUpdatedBefore(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            List<User> users = userRepository.findByUpdatedAtBefore(date);
            if (!users.isEmpty()) {
                userRepository.deleteAll(users);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // DELETE - Bulk delete users
    @DeleteMapping("/bulk-delete")
    public ResponseEntity<HttpStatus> bulkDeleteUsers(@RequestParam("ids") List<Long> ids) {
        try {
            List<User> usersToDelete = ids.stream()
                    .map(id -> userRepository.findById(id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            if (!usersToDelete.isEmpty()) {
                userRepository.deleteAll(usersToDelete);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // DELETE - Delete inactive users (not updated in last 90 days)
    @DeleteMapping("/inactive")
    public ResponseEntity<HttpStatus> deleteInactiveUsers() {
        try {
            LocalDateTime ninetyDaysAgo = LocalDateTime.now().minusDays(90);
            List<User> users = userRepository.findByUpdatedAtBefore(ninetyDaysAgo);
            if (!users.isEmpty()) {
                userRepository.deleteAll(users);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // STATISTICS - Get user count
    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        try {
            long count = userRepository.count();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // STATISTICS - Get user count by role
    @GetMapping("/count/role/{role}")
    public ResponseEntity<Long> getUserCountByRole(@PathVariable("role") UserRole role) {
        try {
            Long count = userRepository.countByRole(role);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // STATISTICS - Get admin count
    @GetMapping("/count/admins")
    public ResponseEntity<Long> getAdminCount() {
        try {
            Long count = userRepository.countByRole(UserRole.ADMIN);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // STATISTICS - Get regular user count
    @GetMapping("/count/regular-users")
    public ResponseEntity<Long> getRegularUserCount() {
        try {
            Long count = userRepository.countByRole(UserRole.USER);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // STATISTICS - Get users created today
    @GetMapping("/count/created-today")
    public ResponseEntity<Long> getUsersCreatedToday() {
        try {
            LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfDay = startOfDay.plusDays(1);
            Long count = userRepository.countByCreatedAtBetween(startOfDay, endOfDay);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // STATISTICS - Get users created this week
    @GetMapping("/count/created-this-week")
    public ResponseEntity<Long> getUsersCreatedThisWeek() {
        try {
            LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
            Long count = userRepository.countByCreatedAtAfter(weekAgo);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // STATISTICS - Get users created this month
    @GetMapping("/count/created-this-month")
    public ResponseEntity<Long> getUsersCreatedThisMonth() {
        try {
            LocalDateTime monthAgo = LocalDateTime.now().minusDays(30);
            Long count = userRepository.countByCreatedAtAfter(monthAgo);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // STATISTICS - Get comprehensive user statistics
    @GetMapping("/statistics")
    public ResponseEntity<UserStatistics> getUserStatistics() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime dayAgo = now.minusDays(1);
            LocalDateTime weekAgo = now.minusDays(7);
            LocalDateTime monthAgo = now.minusDays(30);

            UserStatistics stats = UserStatistics.builder()
                    .totalUsers(userRepository.count())
                    .adminUsers(userRepository.countByRole(UserRole.ADMIN))
                    .regularUsers(userRepository.countByRole(UserRole.USER))
                    .usersCreatedToday(userRepository.countByCreatedAtAfter(dayAgo))
                    .usersCreatedThisWeek(userRepository.countByCreatedAtAfter(weekAgo))
                    .usersCreatedThisMonth(userRepository.countByCreatedAtAfter(monthAgo))
                    .usersUpdatedToday(userRepository.countByUpdatedAtAfter(dayAgo))
                    .usersUpdatedThisWeek(userRepository.countByUpdatedAtAfter(weekAgo))
                    .usersUpdatedThisMonth(userRepository.countByUpdatedAtAfter(monthAgo))
                    .build();

            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // AUTHENTICATION - Login user (basic check)
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody LoginRequest request) {
        try {
            Optional<User> user = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
            if (user.isPresent()) {
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // AUTHENTICATION - Login user by email
    @PostMapping("/login-email")
    public ResponseEntity<User> loginUserByEmail(@RequestBody LoginRequest request) {
        try {
            Optional<User> user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());

            if (user.isPresent()) {
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // VALIDATION - Validate user credentials
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        try {
            Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
            return new ResponseEntity<>(user.isPresent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // VALIDATION - Check if user is admin
    @GetMapping("/{id}/is-admin")
    public ResponseEntity<Boolean> isUserAdmin(@PathVariable("id") Long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                boolean isAdmin = user.get().getRole() == UserRole.ADMIN;
                return new ResponseEntity<>(isAdmin, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // VALIDATION - Check if username is available
    @GetMapping("/available/username/{username}")
    public ResponseEntity<Boolean> isUsernameAvailable(@PathVariable("username") String username) {
        try {
            boolean available = !userRepository.existsByUsername(username);
            return new ResponseEntity<>(available, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // VALIDATION - Check if email is available
    @GetMapping("/available/email/{email}")
    public ResponseEntity<Boolean> isEmailAvailable(@PathVariable("email") String email) {
        try {
            boolean available = !userRepository.existsByEmail(email);
            return new ResponseEntity<>(available, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
