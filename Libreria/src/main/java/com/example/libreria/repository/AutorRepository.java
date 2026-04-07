package com.example.libreria.repository;

import com.example.libreria.domain.Autor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

    List<Autor> findByNacionalidad(String nacionalidad);
    Page<Autor> findByNacionalidad(String nacionalidad, Pageable pageable);

    List<Autor> findByFechaNacimiento(LocalDate fechaNacimiento);
    Page<Autor> findByFechaNacimiento(LocalDate fechaNacimiento, Pageable pageable);

    List<Autor> findByNombreContainingIgnoreCase(String nombre);
    Page<Autor> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}
