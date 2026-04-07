package com.example.libreria.dto.catalogo;

import com.example.libreria.domain.PrestamoLibro;

import java.time.Instant;
import java.time.LocalDate;

public record PrestamoLibroResponse(
        Long id,
        Long usuarioId,
        String usuarioMail,
        Long libroId,
        String libroTitulo,
        String libroIsbn,
        LocalDate fechaPrestamo,
        LocalDate fechaDevolucionPrevista,
        LocalDate fechaDevolucionReal,
        Instant creado,
        Instant actualizado
) {
    public static PrestamoLibroResponse from(PrestamoLibro prestamoLibro) {
        return new PrestamoLibroResponse(
                prestamoLibro.getId(),
                prestamoLibro.getIdUsuario().getId(),
                prestamoLibro.getIdUsuario().getMail(),
                prestamoLibro.getIdLibro().getId(),
                prestamoLibro.getIdLibro().getTitulo(),
                prestamoLibro.getIdLibro().getIsbn(),
                prestamoLibro.getFechaPrestamo(),
                prestamoLibro.getFechaDevolucionPrevista(),
                prestamoLibro.getFechaDevolucionReal(),
                prestamoLibro.getCreado(),
                prestamoLibro.getActualizado()
        );
    }
}
