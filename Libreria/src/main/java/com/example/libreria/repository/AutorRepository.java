package com.example.libreria.repository;

import com.example.libreria.domain.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

    List<Autor> findByNacionalidad(String nacionalidad);

    List<Autor> findByFechaNacimiento(LocalDate fechaNacimiento);

    List<Autor> findByNombreContainingIgnoreCase(String nombre);
}
