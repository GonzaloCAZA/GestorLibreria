package com.example.libreria.service;

import com.example.libreria.domain.Piso;
import com.example.libreria.domain.Sala;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SalaService {

    List<Sala> findAll();
    Page<Sala> findAll(Pageable pageable);

    Sala findById(Long id);

    Sala save(Sala sala);

    Sala update(Long id, Sala sala);

    void deleteById(Long id);

    List<Sala> findByNombre(String nombre);

    List<Sala> findByNombreContaining(String nombre);
    Page<Sala> findByNombreContaining(String nombre, Pageable pageable);

    List<Sala> findByMaximoPersonas(Integer maximoPersonas);
    Page<Sala> findByMaximoPersonas(Integer maximoPersonas, Pageable pageable);

    List<Sala> findByPiso(Piso piso);
}
