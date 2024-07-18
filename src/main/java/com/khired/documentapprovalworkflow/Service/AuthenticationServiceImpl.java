package com.khired.documentapprovalworkflow.Service;

import com.khired.documentapprovalworkflow.Model.AuthenticationResponse;
import com.khired.documentapprovalworkflow.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    @Autowired
    private IJwtService IJwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserService IUserService;

    @Override
    public AuthenticationResponse register(User user) {
        // Check if username already exists
        if (IUserService.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user (ensure you save the hashed password)
        IUserService.saveUser(user);

        // Generate JWT token
        String token = IJwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

    @Override
    public AuthenticationResponse login(User request) {
        // Find the user by username

        User user = IUserService.findByUsername(request.getUsername());

        // Handle case where user does not exist
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Check if passwords match
        boolean passwordsMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!passwordsMatch) {
            throw new BadCredentialsException("Invalid password");
        }

        // Generate JWT token for the authenticated user
        String token = IJwtService.generateToken(user);

        System.out.println(token);
        return new AuthenticationResponse(token);
    }
}
