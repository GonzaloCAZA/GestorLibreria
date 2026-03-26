package com.example.libreria.controller;

import com.example.libreria.domain.Piso;
import com.example.libreria.dto.catalogo.PisoResponse;
import com.example.libreria.service.PisoService;
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
@RequestMapping("/api/pisos")
public class PisoController {

    private final PisoService pisoService;

    public PisoController(PisoService pisoService) {
        this.pisoService = pisoService;
    }

    @GetMapping
    public ResponseEntity<List<PisoResponse>> findAll() {
        return ResponseEntity.ok(pisoService.findAll().stream().map(PisoResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PisoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(PisoResponse.from(pisoService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<PisoResponse> create(@Valid @RequestBody Piso piso) {
        return ResponseEntity.ok(PisoResponse.from(pisoService.save(piso)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<PisoResponse> update(@PathVariable Long id, @Valid @RequestBody Piso piso) {
        return ResponseEntity.ok(PisoResponse.from(pisoService.update(id, piso)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pisoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
