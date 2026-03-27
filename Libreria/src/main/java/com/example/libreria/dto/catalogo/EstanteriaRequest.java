package com.example.libreria.dto.catalogo;

import jakarta.validation.constraints.NotBlank;

public record EstanteriaRequest(
        @NotBlank String categoria,
        Long pisoId,
        PisoRef idPiso
) {
    public Long resolvePisoId() {
        if (pisoId != null) {
            return pisoId;
        }
        return idPiso != null ? idPiso.id() : null;
    }

    public record PisoRef(Long id) {
    }
}
