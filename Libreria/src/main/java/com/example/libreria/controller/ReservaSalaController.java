package com.example.libreria.controller;

import com.example.libreria.domain.Piso;
import com.example.libreria.domain.ReservarSala;
import com.example.libreria.domain.Sala;
import com.example.libreria.dto.common.PageResponse;
import com.example.libreria.dto.catalogo.ReservaSalaResponse;
import com.example.libreria.service.PisoService;
import com.example.libreria.service.ReservaSalaService;
import com.example.libreria.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
import java.util.List;

@RestController
@Transactional(readOnly = true)
@RequestMapping("/api/reservas-sala")
public class ReservaSalaController {

    private final ReservaSalaService reservaSalaService;
    private final PisoService pisoService;
    private final SalaService salaService;

    public ReservaSalaController(ReservaSalaService reservaSalaService, PisoService pisoService, SalaService salaService) {
        this.reservaSalaService = reservaSalaService;
        this.pisoService = pisoService;
        this.salaService = salaService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<ReservaSalaResponse>> findAll(
            @RequestParam(required = false) Instant fechaReserva,
            @RequestParam(required = false) Instant fechaFinReserva,
            @RequestParam(required = false) Boolean mostrarAnteriores,
            Pageable pageable
    ) {
        if (fechaReserva != null) {
            return ResponseEntity.ok(PageResponse.from(reservaSalaService.findByFechaReserva(fechaReserva, pageable), ReservaSalaResponse::from));
        }
        if (fechaFinReserva != null) {
            return ResponseEntity.ok(PageResponse.from(reservaSalaService.findByFechaFinReserva(fechaFinReserva, pageable), ReservaSalaResponse::from));
        }
        //Para admins se muestran todas para usuarios solo las que estan pendientes
        Page <ReservarSala> reservas = mostrarAnteriores ? reservaSalaService.findByFechaReservaAfter(Instant.now(), pageable)
            : reservaSalaService.findAll(pageable);

        return ResponseEntity.ok(PageResponse.from(reservas, ReservaSalaResponse::from));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReservaSalaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ReservaSalaResponse.from(reservaSalaService.findById(id)));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ReservaSalaResponse> create(@Valid @RequestBody ReservarSala reservarSala) {
        reservarSala.setIdSala(salaService.findById(reservarSala.getIdSala().getId()));
        return ResponseEntity.ok(ReservaSalaResponse.from(reservaSalaService.save(reservarSala)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ReservaSalaResponse> update(@PathVariable Long id, @Valid @RequestBody ReservarSala reservarSala) {
        return ResponseEntity.ok(ReservaSalaResponse.from(reservaSalaService.update(id, reservarSala)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservaSalaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
