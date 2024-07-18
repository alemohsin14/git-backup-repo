package com.khired.documentapprovalworkflow.Service;

import com.khired.documentapprovalworkflow.Model.User;
import io.jsonwebtoken.Claims;

import java.util.function.Function;

public interface IJwtService {
    String generateToken(User user);
    <T> T extractClaim(String token, Function<Claims, T> resolver);
    String extractUsername(String token);
    boolean isValid(String token, User user);
}
