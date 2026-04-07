package com.example.libreria.repository;

import com.example.libreria.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByMail(String mail);

    boolean existsByMail(String mail);

    List<Usuario> findByRol(String rol);
    Page<Usuario> findByRol(String rol, Pageable pageable);

    List<Usuario> findByMoroso(Boolean moroso);
    Page<Usuario> findByMoroso(Boolean moroso, Pageable pageable);
}
