package com.example.libreria.service;

import com.example.libreria.domain.Usuario;
import com.example.libreria.dto.auth.AuthResponse;
import com.example.libreria.dto.auth.LoginRequest;
import com.example.libreria.dto.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request, HttpServletResponse response);

    AuthResponse login(LoginRequest request, HttpServletResponse response);

    void logout(HttpServletResponse response);

    Usuario getAuthenticatedUser();
}
