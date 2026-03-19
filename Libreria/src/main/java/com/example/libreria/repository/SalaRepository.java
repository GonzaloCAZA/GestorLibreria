package com.example.libreria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    List<Sala> findByNombre(String nombre);

    List<Sala> findByNombreContainingIgnoreCase(String nombre);

    List<Sala> findByMaximoPersonas(Integer maximoPersonas);

    List<Sala> findByIdPiso(Piso idPiso);
}
