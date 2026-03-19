package com.example.libreria.service;

import com.example.libreria.domain.Piso;

import java.util.List;

public interface PisoService {

    List<Piso> findAll();

    Piso findById(Long id);

    Piso save(Piso piso);

    Piso update(Long id, Piso piso);

    void deleteById(Long id);

    Piso findByNumPiso(Integer numPiso);

    List<Piso> findByNombre(String nombre);

    List<Piso> findByNombreContaining(String nombre);
}
