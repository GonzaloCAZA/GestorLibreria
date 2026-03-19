package com.example.libreria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PrestamoLibroRepository extends JpaRepository<PrestamoLibro, Long> {

    List<PrestamoLibro> findByIdUsuario(Usuario idUsuario);

    List<PrestamoLibro> findByIdLibro(Libro idLibro);

    List<PrestamoLibro> findByFechaPrestamo(LocalDate fechaPrestamo);

    List<PrestamoLibro> findByFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista);

    List<PrestamoLibro> findByFechaDevolucionReal(LocalDate fechaDevolucionReal);
}
