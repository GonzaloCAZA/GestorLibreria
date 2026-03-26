package com.example.libreria.service;

import com.example.libreria.domain.Usuario;
import com.example.libreria.dto.usuario.UsuarioCreateRequest;
import com.example.libreria.dto.usuario.UsuarioUpdateRequest;

import java.util.List;

public interface UsuarioService {

    List<Usuario> findAll();

    Usuario findById(Long id);

    Usuario save(UsuarioCreateRequest request);

    Usuario update(Long id, UsuarioUpdateRequest request);

    void deleteById(Long id);

    Usuario findByMail(String mail);

    List<Usuario> findByRol(String rol);

    List<Usuario> findByMoroso(Boolean moroso);
}
