package com.khired.documentapprovalworkflow.Service;

import com.khired.documentapprovalworkflow.Model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User saveUser(User user);
    void deleteUser(Long id);
    boolean existsById(Long id);
    boolean existsByUsername(String username);
    User findByUsername(String username);
}
