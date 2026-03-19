package com.example.libreria.service;

import com.example.libreria.domain.Usuario;
import com.example.libreria.security.dto.AuthResponse;
import com.example.libreria.security.dto.LoginRequest;
import com.example.libreria.security.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request, HttpServletResponse response);

    AuthResponse login(LoginRequest request, HttpServletResponse response);

    void logout(HttpServletResponse response);

    Usuario getAuthenticatedUser();
}
