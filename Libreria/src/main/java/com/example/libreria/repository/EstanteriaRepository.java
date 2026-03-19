package com.example.libreria.repository;

import com.example.libreria.domain.Estanteria;
import com.example.libreria.domain.Piso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstanteriaRepository extends JpaRepository<Estanteria, Long> {

    List<Estanteria> findByCategoria(String categoria);

    List<Estanteria> findByCategoriaContainingIgnoreCase(String categoria);

    List<Estanteria> findByIdPiso(Piso idPiso);
}
