package com.example.libreria.service;

import com.example.libreria.domain.Piso;
import com.example.libreria.domain.Sala;

import java.util.List;

public interface SalaService {

    List<Sala> findAll();

    Sala findById(Long id);

    Sala save(Sala sala);

    Sala update(Long id, Sala sala);

    void deleteById(Long id);

    List<Sala> findByNombre(String nombre);

    List<Sala> findByNombreContaining(String nombre);

    List<Sala> findByMaximoPersonas(Integer maximoPersonas);

    List<Sala> findByPiso(Piso piso);
}
