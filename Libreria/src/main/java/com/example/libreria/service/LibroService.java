package com.example.libreria.service;

import com.example.libreria.domain.Autor;
import com.example.libreria.domain.Balda;
import com.example.libreria.domain.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LibroService {

    List<Libro> findAll();
    Page<Libro> findAll(Pageable pageable);

    Libro findById(Long id);

    Libro save(Libro libro);

    Libro update(Long id, Libro libro);

    void deleteById(Long id);

    Libro findByIsbn(String isbn);

    List<Libro> findByTitulo(String titulo);

    List<Libro> findByTituloContaining(String titulo);
    Page<Libro> findByTituloContaining(String titulo, Pageable pageable);

    List<Libro> findByAutor(Autor autor);

    List<Libro> findByBalda(Balda balda);

    Page<Libro> findByCategoria(String categoria, Pageable pageable);
}
