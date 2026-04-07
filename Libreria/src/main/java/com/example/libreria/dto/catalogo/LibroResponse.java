package com.example.libreria.dto.catalogo;

import com.example.libreria.domain.Libro;

import java.time.Instant;

public record LibroResponse(
        Long id,
        String titulo,
        String isbn,
        Long autorId,
        String autorNombre,
        Long baldaId,
        Integer baldaNumero,
        Long estanteriaId,
        String estanteriaCategoria,
        Long pisoId,
        Integer pisoNumero,
        String pisoNombre,
        Instant creado,
        Instant actualizado,
        boolean isPrestado
) {
    public static LibroResponse from(Libro libro, boolean isPrestado) {
        return new LibroResponse(
                libro.getId(),
                libro.getTitulo(),
                libro.getIsbn(),
                libro.getIdAutor().getId(),
                libro.getIdAutor().getNombre(),
                libro.getIdBalda().getId(),
                libro.getIdBalda().getNumero(),
                libro.getIdBalda().getIdEstanteria().getId(),
                libro.getIdBalda().getIdEstanteria().getCategoria(),
                libro.getIdBalda().getIdEstanteria().getIdPiso().getId(),
                libro.getIdBalda().getIdEstanteria().getIdPiso().getNumPiso(),
                libro.getIdBalda().getIdEstanteria().getIdPiso().getNombre(),
                libro.getCreado(),
                libro.getActualizado(),
                isPrestado
        );
    }
}
