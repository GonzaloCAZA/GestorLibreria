package com.example.libreria.dto.catalogo;

import com.example.libreria.domain.Estanteria;

import java.time.Instant;

public record EstanteriaResponse(
        Long id,
        String categoria,
        Long pisoId,
        Integer pisoNumero,
        String pisoNombre,
        Instant creado,
        Instant actualizado
) {
    public static EstanteriaResponse from(Estanteria estanteria) {
        return new EstanteriaResponse(
                estanteria.getId(),
                estanteria.getCategoria(),
                estanteria.getIdPiso().getId(),
                estanteria.getIdPiso().getNumPiso(),
                estanteria.getIdPiso().getNombre(),
                estanteria.getCreado(),
                estanteria.getActualizado()
        );
    }
}
