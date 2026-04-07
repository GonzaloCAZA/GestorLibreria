package com.example.libreria.controller;

import com.example.libreria.domain.Balda;
import com.example.libreria.domain.Estanteria;
import com.example.libreria.domain.Libro;
import com.example.libreria.dto.catalogo.LibroResponse;
import com.example.libreria.dto.common.PageResponse;
import com.example.libreria.service.BaldaService;
import com.example.libreria.service.EstanteriaService;
import com.example.libreria.service.LibroService;
import com.example.libreria.service.PrestamoLibroService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
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
    private final EstanteriaService estanteriaService;
    private final BaldaService baldaService;
    private final PrestamoLibroService prestamoLibroService;

    public LibroController(
            LibroService libroService,
            EstanteriaService estanteriaService,
            BaldaService baldaService,
            PrestamoLibroService prestamoLibroService
    ) {
        this.libroService = libroService;
        this.estanteriaService = estanteriaService;
        this.baldaService = baldaService;
        this.prestamoLibroService = prestamoLibroService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<LibroResponse>> findAll(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String categoria,
            Pageable pageable
    ) {
        if (categoria != null && !categoria.isBlank()) {
            return ResponseEntity.ok(PageResponse.from(
                    libroService.findByCategoria(categoria, pageable),
                    libro -> LibroResponse.from(libro, prestamoLibroService.isPrestado(libro.getId()))
            ));
        }
        if (titulo != null && !titulo.isBlank()) {
            return ResponseEntity.ok(PageResponse.from(
                    libroService.findByTituloContaining(titulo, pageable),
                    libro -> LibroResponse.from(libro, prestamoLibroService.isPrestado(libro.getId()))
            ));
        }
        return ResponseEntity.ok(PageResponse.from(
                libroService.findAll(pageable),
                libro -> LibroResponse.from(libro, prestamoLibroService.isPrestado(libro.getId()))
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> findById(@PathVariable Long id) {
        Libro libro = libroService.findById(id);
        return ResponseEntity.ok(LibroResponse.from(libro, prestamoLibroService.isPrestado(libro.getId())));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<LibroResponse> findByIsbn(@PathVariable String isbn) {
        Libro libro = libroService.findByIsbn(isbn);
        return ResponseEntity.ok(LibroResponse.from(libro, prestamoLibroService.isPrestado(libro.getId())));
    }

    @GetMapping("/categorias/{categoria}")
    public ResponseEntity<PageResponse<LibroResponse>> findByCategoria(@PathVariable String categoria, Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(
                libroService.findByCategoria(categoria, pageable),
                libro -> LibroResponse.from(libro, prestamoLibroService.isPrestado(libro.getId()))
        ));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<LibroResponse> create(@Valid @RequestBody Libro libro) {
        libro.setIdBalda(baldaService.findById(libro.getIdBalda().getId()));
        Libro libroGuardado = libroService.save(libro);
        return ResponseEntity.ok(LibroResponse.from(libroGuardado, prestamoLibroService.isPrestado(libroGuardado.getId())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<LibroResponse> update(@PathVariable Long id, @Valid @RequestBody Libro libro) {
        Libro libroActualizado = libroService.update(id, libro);
        return ResponseEntity.ok(LibroResponse.from(libroActualizado, prestamoLibroService.isPrestado(libroActualizado.getId())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        libroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
