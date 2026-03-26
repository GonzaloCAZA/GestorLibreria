package com.example.libreria.controller;

import com.example.libreria.domain.Libro;
import com.example.libreria.dto.catalogo.LibroResponse;
import com.example.libreria.dto.common.PageResponse;
import com.example.libreria.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(readOnly = true)
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<LibroResponse>> findAll(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String categoria,
            Pageable pageable
    ) {
        if (categoria != null && !categoria.isBlank()) {
            return ResponseEntity.ok(PageResponse.from(libroService.findByCategoria(categoria, pageable), LibroResponse::from));
        }
        if (titulo != null && !titulo.isBlank()) {
            return ResponseEntity.ok(PageResponse.from(libroService.findByTituloContaining(titulo, pageable), LibroResponse::from));
        }
        return ResponseEntity.ok(PageResponse.from(libroService.findAll(pageable), LibroResponse::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(LibroResponse.from(libroService.findById(id)));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<LibroResponse> findByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(LibroResponse.from(libroService.findByIsbn(isbn)));
    }

    @GetMapping("/categorias/{categoria}")
    public ResponseEntity<PageResponse<LibroResponse>> findByCategoria(@PathVariable String categoria, Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(libroService.findByCategoria(categoria, pageable), LibroResponse::from));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<LibroResponse> create(@Valid @RequestBody Libro libro) {
        return ResponseEntity.ok(LibroResponse.from(libroService.save(libro)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<LibroResponse> update(@PathVariable Long id, @Valid @RequestBody Libro libro) {
        return ResponseEntity.ok(LibroResponse.from(libroService.update(id, libro)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        libroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
