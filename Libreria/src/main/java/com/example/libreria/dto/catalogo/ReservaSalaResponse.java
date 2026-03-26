package com.example.libreria.dto.catalogo;

import com.example.libreria.domain.ReservarSala;

import java.time.Instant;

public record ReservaSalaResponse(
        Long id,
        Long usuarioId,
        String usuarioMail,
        Long salaId,
        String salaNombre,
        Integer salaMaximoPersonas,
        Long pisoId,
        Integer pisoNumero,
        String pisoNombre,
        Instant fechaReserva,
        Instant fechaFinReserva,
        Instant creado,
        Instant actualizado
) {
    public static ReservaSalaResponse from(ReservarSala reservarSala) {
        return new ReservaSalaResponse(
                reservarSala.getId(),
                reservarSala.getIdUsuario().getId(),
                reservarSala.getIdUsuario().getMail(),
                reservarSala.getIdSala().getId(),
                reservarSala.getIdSala().getNombre(),
                reservarSala.getIdSala().getMaximoPersonas(),
                reservarSala.getIdSala().getIdPiso().getId(),
                reservarSala.getIdSala().getIdPiso().getNumPiso(),
                reservarSala.getIdSala().getIdPiso().getNombre(),
                reservarSala.getFechaReserva(),
                reservarSala.getFechaFinReserva(),
                reservarSala.getCreado(),
                reservarSala.getActualizado()
        );
    }
}
