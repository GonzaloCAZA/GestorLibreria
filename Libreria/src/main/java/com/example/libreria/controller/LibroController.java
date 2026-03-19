package com.example.libreria.controller;

import com.example.libreria.domain.Libro;
import com.example.libreria.service.LibroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public ResponseEntity<List<Libro>> findAll(@RequestParam(required = false) String titulo) {
        if (titulo != null && !titulo.isBlank()) {
            return ResponseEntity.ok(libroService.findByTituloContaining(titulo));
        }
        return ResponseEntity.ok(libroService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> findById(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.findById(id));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Libro> findByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(libroService.findByIsbn(isbn));
    }

    @PostMapping
    public ResponseEntity<Libro> create(@RequestBody Libro libro) {
        return ResponseEntity.ok(libroService.save(libro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> update(@PathVariable Long id, @RequestBody Libro libro) {
        return ResponseEntity.ok(libroService.update(id, libro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        libroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
