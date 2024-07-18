package com.khired.documentapprovalworkflow.Controller;

import com.khired.documentapprovalworkflow.Service.IAuthenticationService;
import com.khired.documentapprovalworkflow.Model.AuthenticationResponse;
import com.khired.documentapprovalworkflow.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling user authentication operations.
 */
@RestController
public class AuthenticationController {

    @Autowired
    private IAuthenticationService iAuthenticationService;

    /**
     * Endpoint for user registration.
     *
     * @param user The user object containing registration details.
     * @return ResponseEntity containing an AuthenticationResponse upon successful registration.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(iAuthenticationService.register(user));
    }

    /**
     * Endpoint for user login.
     *
     * @param user The user object containing login credentials.
     * @return ResponseEntity containing an AuthenticationResponse upon successful login.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User user) {
        return ResponseEntity.ok(iAuthenticationService.login(user));
    }
}
