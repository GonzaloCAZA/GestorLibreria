package com.example.libreria.dto.catalogo;

import com.example.libreria.domain.Piso;

import java.time.Instant;

public record PisoResponse(
        Long id,
        Integer numPiso,
        String nombre,
        Instant creado,
        Instant actualizado
) {
    public static PisoResponse from(Piso piso) {
        return new PisoResponse(
                piso.getId(),
                piso.getNumPiso(),
                piso.getNombre(),
                piso.getCreado(),
                piso.getActualizado()
        );
    }
}
