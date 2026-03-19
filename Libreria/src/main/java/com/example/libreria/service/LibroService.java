package com.example.libreria.service;

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
