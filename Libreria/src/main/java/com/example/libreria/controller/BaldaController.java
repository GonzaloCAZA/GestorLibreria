package com.example.libreria.controller;

import com.example.libreria.domain.Balda;
import com.example.libreria.service.BaldaService;
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
@RequestMapping("/api/baldas")
public class BaldaController {

    private final BaldaService baldaService;

    public BaldaController(BaldaService baldaService) {
        this.baldaService = baldaService;
    }

    @GetMapping
    public ResponseEntity<List<Balda>> findAll() {
        return ResponseEntity.ok(baldaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Balda> findById(@PathVariable Long id) {
        return ResponseEntity.ok(baldaService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Balda> create(@Valid @RequestBody Balda balda) {
        return ResponseEntity.ok(baldaService.save(balda));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Balda> update(@PathVariable Long id, @Valid @RequestBody Balda balda) {
        return ResponseEntity.ok(baldaService.update(id, balda));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        baldaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
