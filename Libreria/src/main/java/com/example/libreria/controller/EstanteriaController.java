package com.example.libreria.controller;

import com.example.libreria.domain.Estanteria;
import com.example.libreria.service.EstanteriaService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estanterias")
public class EstanteriaController {

    private final EstanteriaService estanteriaService;

    public EstanteriaController(EstanteriaService estanteriaService) {
        this.estanteriaService = estanteriaService;
    }

    @GetMapping
    public ResponseEntity<List<Estanteria>> findAll() {
        return ResponseEntity.ok(estanteriaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estanteria> findById(@PathVariable Long id) {
        return ResponseEntity.ok(estanteriaService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Estanteria> create(@Valid @RequestBody Estanteria estanteria) {
        return ResponseEntity.ok(estanteriaService.save(estanteria));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Estanteria> update(@PathVariable Long id, @Valid @RequestBody Estanteria estanteria) {
        return ResponseEntity.ok(estanteriaService.update(id, estanteria));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        estanteriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
