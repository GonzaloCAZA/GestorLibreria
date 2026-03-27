package com.example.libreria.controller;

import com.example.libreria.dto.catalogo.EstanteriaRequest;
import com.example.libreria.dto.catalogo.EstanteriaResponse;
import com.example.libreria.service.EstanteriaService;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional(readOnly = true)
@RequestMapping("/api/estanterias")
public class EstanteriaController {

    private final EstanteriaService estanteriaService;

    public EstanteriaController(EstanteriaService estanteriaService) {
        this.estanteriaService = estanteriaService;
    }

    @GetMapping
    public ResponseEntity<List<EstanteriaResponse>> findAll() {
        return ResponseEntity.ok(estanteriaService.findAll().stream().map(EstanteriaResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstanteriaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(EstanteriaResponse.from(estanteriaService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<EstanteriaResponse> create(@Valid @RequestBody EstanteriaRequest request) {
        return ResponseEntity.ok(EstanteriaResponse.from(estanteriaService.save(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<EstanteriaResponse> update(@PathVariable Long id, @Valid @RequestBody EstanteriaRequest request) {
        return ResponseEntity.ok(EstanteriaResponse.from(estanteriaService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        estanteriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
