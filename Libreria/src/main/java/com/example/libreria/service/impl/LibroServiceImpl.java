package com.example.libreria.service.impl;

import com.example.libreria.domain.Autor;
import com.example.libreria.domain.Balda;
import com.example.libreria.domain.Libro;
import com.example.libreria.repository.LibroRepository;
import com.example.libreria.service.LibroService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;

    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Libro> findAll() {
        return libroRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Libro findById(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Libro no encontrado con id: " + id));
    }

    @Override
    public Libro save(Libro libro) {
        return libroRepository.save(libro);
    }

    @Override
    public Libro update(Long id, Libro libro) {
        Libro libroExistente = findById(id);
        libroExistente.setTitulo(libro.getTitulo());
        libroExistente.setIsbn(libro.getIsbn());
        libroExistente.setIdAutor(libro.getIdAutor());
        libroExistente.setIdBalda(libro.getIdBalda());
        libroExistente.setCreado(libro.getCreado());
        libroExistente.setActualizado(libro.getActualizado());
        return libroRepository.save(libroExistente);
    }

    @Override
    public void deleteById(Long id) {
        Libro libro = findById(id);
        libroRepository.delete(libro);
    }

    @Override
    @Transactional(readOnly = true)
    public Libro findByIsbn(String isbn) {
        return libroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new EntityNotFoundException("Libro no encontrado con isbn: " + isbn));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Libro> findByTitulo(String titulo) {
        return libroRepository.findByTitulo(titulo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Libro> findByTituloContaining(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Libro> findByAutor(Autor autor) {
        return libroRepository.findByIdAutor(autor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Libro> findByBalda(Balda balda) {
        return libroRepository.findByIdBalda(balda);
    }
}
