package com.example.libreria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByIsbn(String isbn);

    List<Libro> findByTitulo(String titulo);

    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    List<Libro> findByIdAutor(Autor idAutor);

    List<Libro> findByIdBalda(Balda idBalda);
}
