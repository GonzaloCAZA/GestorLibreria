package com.example.libreria.dto.catalogo;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PrestamoLibroDevolucionRequest(
        @NotNull LocalDate fechaDevolucionReal
) {
}
