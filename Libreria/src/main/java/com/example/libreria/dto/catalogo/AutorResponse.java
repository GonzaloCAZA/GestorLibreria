package com.example.libreria.dto.catalogo;

import com.example.libreria.domain.Autor;

import java.time.Instant;
import java.time.LocalDate;

public record AutorResponse(
        Long id,
        String nombre,
        LocalDate fechaNacimiento,
        String nacionalidad,
        Instant creado,
        Instant actualizado
) {
    public static AutorResponse from(Autor autor) {
        return new AutorResponse(
                autor.getId(),
                autor.getNombre(),
                autor.getFechaNacimiento(),
                autor.getNacionalidad(),
                autor.getCreado(),
                autor.getActualizado()
        );
    }
}
