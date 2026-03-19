package com.example.libreria.service.impl;

import com.example.libreria.domain.CodigoRecuperacion;
import com.example.libreria.util.Rol;
import com.example.libreria.domain.Usuario;
import com.example.libreria.repository.CodigoRecuperacionRepository;
import com.example.libreria.repository.UsuarioRepository;
import com.example.libreria.security.JwtService;
import com.example.libreria.security.dto.AuthResponse;
import com.example.libreria.security.dto.ForgotPasswordRequest;
import com.example.libreria.security.dto.LoginRequest;
import com.example.libreria.security.dto.RegisterRequest;
import com.example.libreria.security.dto.ResetPasswordRequest;
import com.example.libreria.service.AuthService;
import com.example.libreria.service.MailService;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final CodigoRecuperacionRepository codigoRecuperacionRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailService mailService;

    public AuthServiceImpl(
            UsuarioRepository usuarioRepository,
            CodigoRecuperacionRepository codigoRecuperacionRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            MailService mailService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.codigoRecuperacionRepository = codigoRecuperacionRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.mailService = mailService;
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

        return new AuthResponse(savedUsuario.getId(), savedUsuario.getMail(), savedUsuario.getRol().name(), "Usuario registrado correctamente");
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

        return new AuthResponse(usuario.getId(), usuario.getMail(), usuario.getRol().name(), "Login correcto");
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

    @Override
    public void sendRecoveryCode(ForgotPasswordRequest request) {
        codigoRecuperacionRepository.deleteByExpiraEnBefore(Instant.now());

        String email = request.getMail().trim().toLowerCase();
        usuarioRepository.findByMail(email).ifPresent(usuario -> {
            invalidateActiveRecoveryCodes(usuario);

            String code = generateRecoveryCode();
            CodigoRecuperacion recoveryCode = new CodigoRecuperacion();
            recoveryCode.setIdUsuario(usuario);
            recoveryCode.setCodigo(code);
            recoveryCode.setCreado(Instant.now());
            recoveryCode.setExpiraEn(Instant.now().plus(15, ChronoUnit.MINUTES));

            codigoRecuperacionRepository.save(recoveryCode);
            mailService.sendPasswordResetCode(usuario.getMail(), code);
        });
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        String email = request.getMail().trim().toLowerCase();
        Usuario usuario = usuarioRepository.findByMail(email)
                .orElseThrow(() -> new BadCredentialsException("Codigo o correo no valido"));

        CodigoRecuperacion recoveryCode = codigoRecuperacionRepository
                .findTopByIdUsuarioAndCodigoAndUsadoEnIsNullOrderByCreadoDesc(usuario, request.getCodigo().trim())
                .orElseThrow(() -> new BadCredentialsException("Codigo o correo no valido"));

        if (recoveryCode.getExpiraEn().isBefore(Instant.now())) {
            throw new BadCredentialsException("El codigo de recuperacion ha expirado");
        }

        usuario.setPwd(passwordEncoder.encode(request.getNewPassword()));
        usuario.setActualizado(Instant.now());
        usuarioRepository.save(usuario);

        recoveryCode.setUsadoEn(Instant.now());
        codigoRecuperacionRepository.save(recoveryCode);

        invalidateActiveRecoveryCodes(usuario);
    }

    private void authenticateAndAttachToken(String email, String rawPassword, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, rawPassword)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());
        response.addHeader("Set-Cookie", jwtService.buildAuthCookie(token).toString());
    }

    private Rol normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return Rol.ROLE_CUSTOMER;
        }
        switch (role.trim().replace("ROLE_", "")){
            case "ADMIN":
                return Rol.ROLE_ADMIN;
            case "DEV":
                return Rol.ROLE_DEV;
            default:
                return Rol.ROLE_CUSTOMER;
        }
    }

    private void invalidateActiveRecoveryCodes(Usuario usuario) {
        List<CodigoRecuperacion> activeCodes = codigoRecuperacionRepository.findByIdUsuarioAndUsadoEnIsNull(usuario);
        Instant now = Instant.now();
        for (CodigoRecuperacion activeCode : activeCodes) {
            activeCode.setUsadoEn(now);
        }
        codigoRecuperacionRepository.saveAll(activeCodes);
    }

    private String generateRecoveryCode() {
        int value = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return Integer.toString(value);
    }
}
