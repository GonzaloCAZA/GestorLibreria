package com.example.libreria.controller;

import com.example.libreria.domain.ReservarSala;
import com.example.libreria.service.ReservaSalaService;
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
@RequestMapping("/api/reservas-sala")
public class ReservaSalaController {

    private final ReservaSalaService reservaSalaService;

    public ReservaSalaController(ReservaSalaService reservaSalaService) {
        this.reservaSalaService = reservaSalaService;
    }

    @GetMapping
    public ResponseEntity<List<ReservarSala>> findAll() {
        return ResponseEntity.ok(reservaSalaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservarSala> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reservaSalaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReservarSala> create(@RequestBody ReservarSala reservarSala) {
        return ResponseEntity.ok(reservaSalaService.save(reservarSala));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservarSala> update(@PathVariable Long id, @RequestBody ReservarSala reservarSala) {
        return ResponseEntity.ok(reservaSalaService.update(id, reservarSala));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservaSalaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
