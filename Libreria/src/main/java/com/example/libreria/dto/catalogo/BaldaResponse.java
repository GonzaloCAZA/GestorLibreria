package com.example.libreria.dto.catalogo;

import com.example.libreria.domain.Balda;

import java.time.Instant;

public record BaldaResponse(
        Long id,
        Integer numero,
        Long estanteriaId,
        String estanteriaCategoria,
        Long pisoId,
        Integer pisoNumero,
        String pisoNombre,
        Instant creado,
        Instant actualizado
) {
    public static BaldaResponse from(Balda balda) {
        return new BaldaResponse(
                balda.getId(),
                balda.getNumero(),
                balda.getIdEstanteria().getId(),
                balda.getIdEstanteria().getCategoria(),
                balda.getIdEstanteria().getIdPiso().getId(),
                balda.getIdEstanteria().getIdPiso().getNumPiso(),
                balda.getIdEstanteria().getIdPiso().getNombre(),
                balda.getCreado(),
                balda.getActualizado()
        );
    }
}
