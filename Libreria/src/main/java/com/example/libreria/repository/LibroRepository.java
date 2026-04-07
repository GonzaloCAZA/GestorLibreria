package com.example.libreria.repository;

import com.example.libreria.domain.Autor;
import com.example.libreria.domain.Balda;
import com.example.libreria.domain.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByIsbn(String isbn);

    List<Libro> findByTitulo(String titulo);

    List<Libro> findByTituloContainingIgnoreCase(String titulo);
    Page<Libro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);

    List<Libro> findByIdAutor(Autor idAutor);

    List<Libro> findByIdBalda(Balda idBalda);

    Page<Libro> findByIdBalda_IdEstanteria_CategoriaContainingIgnoreCase(String categoria, Pageable pageable);
}
