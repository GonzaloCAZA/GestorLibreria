package com.example.libreria.dto.catalogo;

import com.example.libreria.domain.Sala;

import java.time.Instant;

public record SalaResponse(
        Long id,
        String nombre,
        Integer maximoPersonas,
        Long pisoId,
        Integer pisoNumero,
        String pisoNombre,
        Instant creado,
        Instant actualizado
) {
    public static SalaResponse from(Sala sala) {
        return new SalaResponse(
                sala.getId(),
                sala.getNombre(),
                sala.getMaximoPersonas(),
                sala.getIdPiso().getId(),
                sala.getIdPiso().getNumPiso(),
                sala.getIdPiso().getNombre(),
                sala.getCreado(),
                sala.getActualizado()
        );
    }
}
