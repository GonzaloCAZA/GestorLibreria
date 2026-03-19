package com.example.libreria.service;

import com.example.libreria.domain.Balda;
import com.example.libreria.domain.Estanteria;

import java.util.List;

public interface BaldaService {

    List<Balda> findAll();

    Balda findById(Long id);

    Balda save(Balda balda);

    Balda update(Long id, Balda balda);

    void deleteById(Long id);

    List<Balda> findByNumero(Integer numero);

    List<Balda> findByEstanteria(Estanteria estanteria);
}
