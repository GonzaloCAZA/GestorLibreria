package com.example.libreria.repository;

import com.example.libreria.domain.Libro;
import com.example.libreria.domain.PrestamoLibro;
import com.example.libreria.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PrestamoLibroRepository extends JpaRepository<PrestamoLibro, Long> {

    List<PrestamoLibro> findByIdUsuario(Usuario idUsuario);

    List<PrestamoLibro> findByIdLibro(Libro idLibro);

    List<PrestamoLibro> findByFechaPrestamo(LocalDate fechaPrestamo);
    Page<PrestamoLibro> findByFechaPrestamo(LocalDate fechaPrestamo, Pageable pageable);

    List<PrestamoLibro> findByFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista);
    Page<PrestamoLibro> findByFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista, Pageable pageable);

    List<PrestamoLibro> findByFechaDevolucionReal(LocalDate fechaDevolucionReal);
    Page<PrestamoLibro> findByFechaDevolucionReal(LocalDate fechaDevolucionReal, Pageable pageable);
}
