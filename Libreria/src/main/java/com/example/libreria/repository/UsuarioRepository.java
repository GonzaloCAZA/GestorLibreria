package com.example.libreria.repository;

import com.example.libreria.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByMail(String mail);

    List<Usuario> findByRol(String rol);

    List<Usuario> findByMoroso(Boolean moroso);
}
