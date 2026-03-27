package com.example.libreria.service;

import com.example.libreria.domain.Libro;
import com.example.libreria.domain.PrestamoLibro;
import com.example.libreria.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PrestamoLibroService {

    List<PrestamoLibro> findAll();
    Page<PrestamoLibro> findAll(Pageable pageable);

    PrestamoLibro findById(Long id);

    PrestamoLibro save(PrestamoLibro prestamoLibro);

    PrestamoLibro update(Long id, PrestamoLibro prestamoLibro);

    PrestamoLibro devolver(Long id, LocalDate fechaDevolucionReal);

    void deleteById(Long id);

    List<PrestamoLibro> findByUsuario(Usuario usuario);

    List<PrestamoLibro> findByLibro(Libro libro);

    List<PrestamoLibro> findByFechaPrestamo(LocalDate fechaPrestamo);
    Page<PrestamoLibro> findByFechaPrestamo(LocalDate fechaPrestamo, Pageable pageable);

    List<PrestamoLibro> findByFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista);
    Page<PrestamoLibro> findByFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista, Pageable pageable);

    List<PrestamoLibro> findByFechaDevolucionReal(LocalDate fechaDevolucionReal);
    Page<PrestamoLibro> findByFechaDevolucionReal(LocalDate fechaDevolucionReal, Pageable pageable);
}
