package com.example.libreria.controller;

import com.example.libreria.domain.Autor;
import com.example.libreria.dto.catalogo.AutorResponse;
import com.example.libreria.dto.common.PageResponse;
import com.example.libreria.service.AutorService;
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

import java.time.LocalDate;
@RestController
@Transactional(readOnly = true)
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<AutorResponse>> findAll(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String nacionalidad,
            @RequestParam(required = false) LocalDate fechaNacimiento,
            Pageable pageable
    ) {
        if (nombre != null && !nombre.isBlank()) {
            return ResponseEntity.ok(PageResponse.from(autorService.findByNombreContaining(nombre, pageable), AutorResponse::from));
        }
        if (nacionalidad != null && !nacionalidad.isBlank()) {
            return ResponseEntity.ok(PageResponse.from(autorService.findByNacionalidad(nacionalidad, pageable), AutorResponse::from));
        }
        if (fechaNacimiento != null) {
            return ResponseEntity.ok(PageResponse.from(autorService.findByFechaNacimiento(fechaNacimiento, pageable), AutorResponse::from));
        }
        return ResponseEntity.ok(PageResponse.from(autorService.findAll(pageable), AutorResponse::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(AutorResponse.from(autorService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<AutorResponse> create(@Valid @RequestBody Autor autor) {
        return ResponseEntity.ok(AutorResponse.from(autorService.save(autor)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<AutorResponse> update(@PathVariable Long id, @Valid @RequestBody Autor autor) {
        return ResponseEntity.ok(AutorResponse.from(autorService.update(id, autor)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        autorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
