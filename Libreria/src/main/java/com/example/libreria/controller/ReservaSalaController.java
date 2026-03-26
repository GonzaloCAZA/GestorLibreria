package com.example.libreria.controller;

import com.example.libreria.domain.ReservarSala;
import com.example.libreria.dto.common.PageResponse;
import com.example.libreria.dto.catalogo.ReservaSalaResponse;
import com.example.libreria.service.ReservaSalaService;
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
import java.time.Instant;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@Transactional(readOnly = true)
@RequestMapping("/api/reservas-sala")
public class ReservaSalaController {

    private final ReservaSalaService reservaSalaService;

    public ReservaSalaController(ReservaSalaService reservaSalaService) {
        this.reservaSalaService = reservaSalaService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<ReservaSalaResponse>> findAll(
            @RequestParam(required = false) Instant fechaReserva,
            @RequestParam(required = false) Instant fechaFinReserva,
            Pageable pageable
    ) {
        if (fechaReserva != null) {
            return ResponseEntity.ok(PageResponse.from(reservaSalaService.findByFechaReserva(fechaReserva, pageable), ReservaSalaResponse::from));
        }
        if (fechaFinReserva != null) {
            return ResponseEntity.ok(PageResponse.from(reservaSalaService.findByFechaFinReserva(fechaFinReserva, pageable), ReservaSalaResponse::from));
        }
        return ResponseEntity.ok(PageResponse.from(reservaSalaService.findAll(pageable), ReservaSalaResponse::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaSalaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ReservaSalaResponse.from(reservaSalaService.findById(id)));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ReservaSalaResponse> create(@Valid @RequestBody ReservarSala reservarSala) {
        return ResponseEntity.ok(ReservaSalaResponse.from(reservaSalaService.save(reservarSala)));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ReservaSalaResponse> update(@PathVariable Long id, @Valid @RequestBody ReservarSala reservarSala) {
        return ResponseEntity.ok(ReservaSalaResponse.from(reservaSalaService.update(id, reservarSala)));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservaSalaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
