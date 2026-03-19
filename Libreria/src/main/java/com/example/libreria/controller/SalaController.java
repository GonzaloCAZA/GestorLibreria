package com.example.libreria.controller;

import com.example.libreria.domain.Sala;
import com.example.libreria.service.SalaService;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/salas")
public class SalaController {

    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @GetMapping
    public ResponseEntity<List<Sala>> findAll() {
        return ResponseEntity.ok(salaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sala> findById(@PathVariable Long id) {
        return ResponseEntity.ok(salaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Sala> create(@RequestBody Sala sala) {
        return ResponseEntity.ok(salaService.save(sala));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sala> update(@PathVariable Long id, @RequestBody Sala sala) {
        return ResponseEntity.ok(salaService.update(id, sala));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
