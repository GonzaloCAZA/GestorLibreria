package com.example.libreria.service;

import com.example.libreria.domain.Autor;
import com.example.libreria.domain.Balda;
import com.example.libreria.domain.Libro;

import java.util.List;

public interface LibroService {

    List<Libro> findAll();

    Libro findById(Long id);

    Libro save(Libro libro);

    Libro update(Long id, Libro libro);

    void deleteById(Long id);

    Libro findByIsbn(String isbn);

    List<Libro> findByTitulo(String titulo);

    List<Libro> findByTituloContaining(String titulo);

    List<Libro> findByAutor(Autor autor);

    List<Libro> findByBalda(Balda balda);
}
