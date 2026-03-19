package com.example.libreria.controller;

import com.example.libreria.domain.Balda;
import com.example.libreria.service.BaldaService;
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
    public ResponseEntity<Balda> create(@RequestBody Balda balda) {
        return ResponseEntity.ok(baldaService.save(balda));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Balda> update(@PathVariable Long id, @RequestBody Balda balda) {
        return ResponseEntity.ok(baldaService.update(id, balda));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        baldaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
