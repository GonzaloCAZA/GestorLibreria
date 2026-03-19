package com.example.libreria.service.impl;

import com.example.libreria.repository.UsuarioRepository;
import com.example.libreria.security.JwtService;
import com.example.libreria.security.dto.AuthResponse;
import com.example.libreria.security.dto.LoginRequest;
import com.example.libreria.security.dto.RegisterRequest;
import com.example.libreria.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UsuarioRepository usuarioRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse register(RegisterRequest request, HttpServletResponse response) {
        if (usuarioRepository.existsByMail(request.getMail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        Usuario usuario = new Usuario();
        usuario.setMail(request.getMail().trim().toLowerCase());
        usuario.setPwd(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(normalizeRole(request.getRol()));
        usuario.setMoroso(Boolean.FALSE);
        usuario.setCreado(Instant.now());
        usuario.setActualizado(Instant.now());

        Usuario savedUsuario = usuarioRepository.save(usuario);
        authenticateAndAttachToken(savedUsuario.getMail(), request.getPassword(), response);

        return new AuthResponse(savedUsuario.getId(), savedUsuario.getMail(), savedUsuario.getRol(), "Usuario registrado correctamente");
    }

    @Override
    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getMail().trim().toLowerCase(), request.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());
        response.addHeader("Set-Cookie", jwtService.buildAuthCookie(token).toString());

        Usuario usuario = usuarioRepository.findByMail(request.getMail().trim().toLowerCase())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        return new AuthResponse(usuario.getId(), usuario.getMail(), usuario.getRol(), "Login correcto");
    }

    @Override
    public void logout(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        response.addHeader("Set-Cookie", jwtService.buildLogoutCookie().toString());
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BadCredentialsException("No hay usuario autenticado");
        }

        String email = authentication.getName();
        return usuarioRepository.findByMail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con mail: " + email));
    }

    private void authenticateAndAttachToken(String email, String rawPassword, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, rawPassword)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());
        response.addHeader("Set-Cookie", jwtService.buildAuthCookie(token).toString());
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "USER";
        }
        return role.trim().replace("ROLE_", "");
    }
}
