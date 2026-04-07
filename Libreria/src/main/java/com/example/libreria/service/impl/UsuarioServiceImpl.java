package com.example.libreria.service.impl;

import com.example.libreria.domain.Usuario;
import com.example.libreria.dto.usuario.UsuarioCreateRequest;
import com.example.libreria.dto.usuario.UsuarioUpdateRequest;
import com.example.libreria.repository.UsuarioRepository;
import com.example.libreria.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Override
    public Usuario save(UsuarioCreateRequest request) {
        if (usuarioRepository.existsByMail(request.getMail().trim().toLowerCase())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        Usuario usuario = new Usuario();
        usuario.setMail(request.getMail().trim().toLowerCase());
        usuario.setRol(request.getRol());
        usuario.setMoroso(request.getMoroso());
        usuario.setPwd(passwordEncoder.encode(request.getPassword()));
        usuario.setCreado(Instant.now());
        usuario.setActualizado(Instant.now());
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario update(Long id, UsuarioUpdateRequest request) {
        Usuario usuarioExistente = findById(id);
        String normalizedMail = request.getMail().trim().toLowerCase();
        if (!usuarioExistente.getMail().equalsIgnoreCase(normalizedMail)
                && usuarioRepository.existsByMail(normalizedMail)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        usuarioExistente.setMail(normalizedMail);
        usuarioExistente.setRol(request.getRol());
        usuarioExistente.setMoroso(request.getMoroso());
        usuarioExistente.setActualizado(Instant.now());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuarioExistente.setPwd(passwordEncoder.encode(request.getPassword()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    public void deleteById(Long id) {
        Usuario usuario = findById(id);
        usuarioRepository.delete(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findByMail(String mail) {
        String normalizedMail = mail.trim().toLowerCase();
        return usuarioRepository.findByMail(normalizedMail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con mail: " + normalizedMail));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findByRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> findByRol(String rol, Pageable pageable) {
        return usuarioRepository.findByRol(rol, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findByMoroso(Boolean moroso) {
        return usuarioRepository.findByMoroso(moroso);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> findByMoroso(Boolean moroso, Pageable pageable) {
        return usuarioRepository.findByMoroso(moroso, pageable);
    }
}
