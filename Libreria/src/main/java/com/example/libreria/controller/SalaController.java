package com.example.libreria.controller;

import com.example.libreria.domain.Sala;
import com.example.libreria.dto.catalogo.SalaResponse;
import com.example.libreria.dto.common.PageResponse;
import com.example.libreria.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(readOnly = true)
@RequestMapping("/api/salas")
public class SalaController {

    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<SalaResponse>> findAll(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer maximoPersonas,
            Pageable pageable
    ) {
        if (nombre != null && !nombre.isBlank()) {
            return ResponseEntity.ok(PageResponse.from(salaService.findByNombreContaining(nombre, pageable), SalaResponse::from));
        }
        if (maximoPersonas != null) {
            return ResponseEntity.ok(PageResponse.from(salaService.findByMaximoPersonas(maximoPersonas, pageable), SalaResponse::from));
        }
        return ResponseEntity.ok(PageResponse.from(salaService.findAll(pageable), SalaResponse::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(SalaResponse.from(salaService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<SalaResponse> create(@Valid @RequestBody Sala sala) {
        return ResponseEntity.ok(SalaResponse.from(salaService.save(sala)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<SalaResponse> update(@PathVariable Long id, @Valid @RequestBody Sala sala) {
        return ResponseEntity.ok(SalaResponse.from(salaService.update(id, sala)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
