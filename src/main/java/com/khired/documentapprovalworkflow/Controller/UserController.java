package com.khired.documentapprovalworkflow.Controller;

import com.khired.documentapprovalworkflow.Service.ICompanyService;
import com.khired.documentapprovalworkflow.Service.IUserService;
import com.khired.documentapprovalworkflow.Model.Company;
import com.khired.documentapprovalworkflow.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for managing user operations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService IUserService;

    @Autowired
    private ICompanyService ICompanyService;

    /**
     * Retrieves all users.
     *
     * @return List of User objects representing all users.
     */
    @GetMapping("/")
    public List<User> getAllUsers() {
        return IUserService.getAllUsers();
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity containing the retrieved User object if found, or 404 Not Found if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = IUserService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new user.
     *
     * @param user The User object containing user details to be created.
     * @return ResponseEntity with the created User object if successful, or a bad request message if validation fails or user already exists.
     */
    @PostMapping("/add")
    public ResponseEntity<?> createUser(@Validated @RequestBody User user) {
        // Check if the companyId exists in the company table
        Optional<Company> company = ICompanyService.getCompanyById(user.getCompanyId());
        if (company.isEmpty()) {
            return ResponseEntity.badRequest().body("Please enter correct Company ID!"); // Handle the case where companyId does not exist
        }

        // Check if the username already exists
        if (IUserService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists!"); // Handle duplicate username scenario
        }

        // Save the user
        User savedUser = IUserService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * Updates an existing user.
     *
     * @param id           The ID of the user to update.
     * @param userDetails  The updated User object containing new user details.
     * @param bindingResult BindingResult for validation errors.
     * @return ResponseEntity with the updated User object if successful, or a bad request message if validation fails or user not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Validated @RequestBody User userDetails, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Optional<User> userOptional = IUserService.getUserById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (!user.getCompanyId().equals(userDetails.getCompanyId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Company ID mismatch");
            }

            // Check if the new username is already used by another user
            if (!user.getUsername().equals(userDetails.getUsername()) && IUserService.existsByUsername(userDetails.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
            }

            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            user.setRole(userDetails.getRole());
            // Assuming companyId and userId remain unchanged

            User updatedUser = IUserService.saveUser(user);
            if (updatedUser != null) {
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     * @return ResponseEntity with success message upon deletion, or 404 Not Found if user not found.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (IUserService.existsById(id)) {
            IUserService.deleteUser(id);
            return ResponseEntity.ok("User deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
