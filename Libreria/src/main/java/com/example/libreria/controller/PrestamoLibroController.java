package com.example.libreria.controller;

import com.example.libreria.domain.PrestamoLibro;
import com.example.libreria.service.PrestamoLibroService;
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
@RequestMapping("/api/prestamos")
public class PrestamoLibroController {

    private final PrestamoLibroService prestamoLibroService;

    public PrestamoLibroController(PrestamoLibroService prestamoLibroService) {
        this.prestamoLibroService = prestamoLibroService;
    }

    @GetMapping
    public ResponseEntity<List<PrestamoLibro>> findAll() {
        return ResponseEntity.ok(prestamoLibroService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoLibro> findById(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoLibroService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PrestamoLibro> create(@RequestBody PrestamoLibro prestamoLibro) {
        return ResponseEntity.ok(prestamoLibroService.save(prestamoLibro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrestamoLibro> update(@PathVariable Long id, @RequestBody PrestamoLibro prestamoLibro) {
        return ResponseEntity.ok(prestamoLibroService.update(id, prestamoLibro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        prestamoLibroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
