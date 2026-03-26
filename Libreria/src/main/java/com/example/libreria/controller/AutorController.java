package com.example.libreria.controller;

import com.example.libreria.domain.Autor;
import com.example.libreria.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public ResponseEntity<List<Autor>> findAll(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String nacionalidad,
            @RequestParam(required = false) LocalDate fechaNacimiento
    ) {
        if (nombre != null && !nombre.isBlank()) {
            return ResponseEntity.ok(autorService.findByNombreContaining(nombre));
        }
        if (nacionalidad != null && !nacionalidad.isBlank()) {
            return ResponseEntity.ok(autorService.findByNacionalidad(nacionalidad));
        }
        if (fechaNacimiento != null) {
            return ResponseEntity.ok(autorService.findByFechaNacimiento(fechaNacimiento));
        }
        return ResponseEntity.ok(autorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> findById(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Autor> create(@Valid @RequestBody Autor autor) {
        return ResponseEntity.ok(autorService.save(autor));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Autor> update(@PathVariable Long id, @Valid @RequestBody Autor autor) {
        return ResponseEntity.ok(autorService.update(id, autor));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        autorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
