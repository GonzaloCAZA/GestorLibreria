package com.example.libreria.repository;

import com.example.libreria.domain.Piso;
import com.example.libreria.domain.Sala;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    List<Sala> findByNombre(String nombre);

    List<Sala> findByNombreContainingIgnoreCase(String nombre);
    Page<Sala> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    List<Sala> findByMaximoPersonas(Integer maximoPersonas);
    Page<Sala> findByMaximoPersonas(Integer maximoPersonas, Pageable pageable);

    List<Sala> findByIdPiso(Piso idPiso);
}
