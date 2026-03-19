package com.example.libreria.service;

import java.util.List;

public interface UsuarioService {

    List<Usuario> findAll();

    Usuario findById(Long id);

    Usuario save(Usuario usuario);

    Usuario update(Long id, Usuario usuario);

    void deleteById(Long id);

    Usuario findByMail(String mail);

    List<Usuario> findByRol(String rol);

    List<Usuario> findByMoroso(Boolean moroso);
}
