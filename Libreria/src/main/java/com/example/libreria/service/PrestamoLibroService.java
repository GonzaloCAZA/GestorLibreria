package com.example.libreria.service;

import java.time.LocalDate;
import java.util.List;

public interface PrestamoLibroService {

    List<PrestamoLibro> findAll();

    PrestamoLibro findById(Long id);

    PrestamoLibro save(PrestamoLibro prestamoLibro);

    PrestamoLibro update(Long id, PrestamoLibro prestamoLibro);

    void deleteById(Long id);

    List<PrestamoLibro> findByUsuario(Usuario usuario);

    List<PrestamoLibro> findByLibro(Libro libro);

    List<PrestamoLibro> findByFechaPrestamo(LocalDate fechaPrestamo);

    List<PrestamoLibro> findByFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista);

    List<PrestamoLibro> findByFechaDevolucionReal(LocalDate fechaDevolucionReal);
}
