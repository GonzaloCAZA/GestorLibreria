package com.example.libreria.service;

import com.example.libreria.domain.Usuario;
import com.example.libreria.dto.usuario.UsuarioCreateRequest;
import com.example.libreria.dto.usuario.UsuarioUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsuarioService {

    List<Usuario> findAll();
    Page<Usuario> findAll(Pageable pageable);

    Usuario findById(Long id);

    Usuario save(UsuarioCreateRequest request);

    Usuario update(Long id, UsuarioUpdateRequest request);

    void deleteById(Long id);

    Usuario findByMail(String mail);

    List<Usuario> findByRol(String rol);
    Page<Usuario> findByRol(String rol, Pageable pageable);

    List<Usuario> findByMoroso(Boolean moroso);
    Page<Usuario> findByMoroso(Boolean moroso, Pageable pageable);
}
