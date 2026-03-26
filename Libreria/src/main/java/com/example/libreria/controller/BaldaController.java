package com.example.libreria.controller;

import com.example.libreria.domain.Balda;
import com.example.libreria.dto.catalogo.BaldaResponse;
import com.example.libreria.service.BaldaService;
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
@RequestMapping("/api/baldas")
public class BaldaController {

    private final BaldaService baldaService;

    public BaldaController(BaldaService baldaService) {
        this.baldaService = baldaService;
    }

    @GetMapping
    public ResponseEntity<List<BaldaResponse>> findAll() {
        return ResponseEntity.ok(baldaService.findAll().stream().map(BaldaResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaldaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(BaldaResponse.from(baldaService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<BaldaResponse> create(@Valid @RequestBody Balda balda) {
        return ResponseEntity.ok(BaldaResponse.from(baldaService.save(balda)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<BaldaResponse> update(@PathVariable Long id, @Valid @RequestBody Balda balda) {
        return ResponseEntity.ok(BaldaResponse.from(baldaService.update(id, balda)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        baldaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
