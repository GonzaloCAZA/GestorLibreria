package com.example.libreria.service.impl;

import com.example.libreria.repository.UsuarioRepository;
import com.example.libreria.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario update(Long id, Usuario usuario) {
        Usuario usuarioExistente = findById(id);
        usuarioExistente.setMail(usuario.getMail());
        usuarioExistente.setRol(usuario.getRol());
        usuarioExistente.setMoroso(usuario.getMoroso());
        usuarioExistente.setCreado(usuario.getCreado());
        usuarioExistente.setActualizado(usuario.getActualizado());
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
        return usuarioRepository.findByMail(mail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con mail: " + mail));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findByRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findByMoroso(Boolean moroso) {
        return usuarioRepository.findByMoroso(moroso);
    }
}
