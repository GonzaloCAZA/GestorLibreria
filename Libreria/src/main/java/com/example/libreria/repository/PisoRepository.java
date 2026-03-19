package com.example.libreria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PisoRepository extends JpaRepository<Piso, Long> {

    Optional<Piso> findByNumPiso(Integer numPiso);

    List<Piso> findByNombre(String nombre);

    List<Piso> findByNombreContainingIgnoreCase(String nombre);
}
