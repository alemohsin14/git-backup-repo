package com.khired.documentapprovalworkflow.Service;


import com.khired.documentapprovalworkflow.Model.AuthenticationResponse;
import com.khired.documentapprovalworkflow.Model.User;

public interface IAuthenticationService {
    AuthenticationResponse register(User user);
    AuthenticationResponse login(User user);
}