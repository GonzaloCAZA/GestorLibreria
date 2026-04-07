package com.example.libreria.controller;

import com.example.libreria.domain.PrestamoLibro;
import com.example.libreria.dto.catalogo.PrestamoLibroDevolucionRequest;
import com.example.libreria.dto.catalogo.PrestamoLibroResponse;
import com.example.libreria.dto.common.PageResponse;
import com.example.libreria.service.PrestamoLibroService;
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
import java.time.LocalDate;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@Transactional(readOnly = true)
@RequestMapping("/api/prestamos")
public class PrestamoLibroController {

    private final PrestamoLibroService prestamoLibroService;

    public PrestamoLibroController(PrestamoLibroService prestamoLibroService) {
        this.prestamoLibroService = prestamoLibroService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<PrestamoLibroResponse>> findAll(
            @RequestParam(required = false) LocalDate fechaPrestamo,
            @RequestParam(required = false) LocalDate fechaDevolucionPrevista,
            @RequestParam(required = false) LocalDate fechaDevolucionReal,
            Pageable pageable
    ) {
        if (fechaPrestamo != null) {
            return ResponseEntity.ok(PageResponse.from(prestamoLibroService.findByFechaPrestamo(fechaPrestamo, pageable), PrestamoLibroResponse::from));
        }
        if (fechaDevolucionPrevista != null) {
            return ResponseEntity.ok(PageResponse.from(prestamoLibroService.findByFechaDevolucionPrevista(fechaDevolucionPrevista, pageable), PrestamoLibroResponse::from));
        }
        if (fechaDevolucionReal != null) {
            return ResponseEntity.ok(PageResponse.from(prestamoLibroService.findByFechaDevolucionReal(fechaDevolucionReal, pageable), PrestamoLibroResponse::from));
        }
        return ResponseEntity.ok(PageResponse.from(prestamoLibroService.findAll(pageable), PrestamoLibroResponse::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoLibroResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(PrestamoLibroResponse.from(prestamoLibroService.findById(id)));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PrestamoLibroResponse> create(@Valid @RequestBody PrestamoLibro prestamoLibro) {
        return ResponseEntity.ok(PrestamoLibroResponse.from(prestamoLibroService.save(prestamoLibro)));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PrestamoLibroResponse> update(@PathVariable Long id, @Valid @RequestBody PrestamoLibro prestamoLibro) {
        return ResponseEntity.ok(PrestamoLibroResponse.from(prestamoLibroService.update(id, prestamoLibro)));
    }

    @PutMapping("/{id}/devolver")
    @Transactional
    public ResponseEntity<PrestamoLibroResponse> devolver(
            @PathVariable Long id,
            @Valid @RequestBody PrestamoLibroDevolucionRequest request
    ) {
        return ResponseEntity.ok(PrestamoLibroResponse.from(
                prestamoLibroService.devolver(id, request.fechaDevolucionReal())
        ));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        prestamoLibroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
