package com.example.libreria.service;

import com.example.libreria.domain.Estanteria;
import com.example.libreria.domain.Piso;
import com.example.libreria.dto.catalogo.EstanteriaRequest;

import java.util.List;

public interface EstanteriaService {

    List<Estanteria> findAll();

    Estanteria findById(Long id);

    Estanteria save(EstanteriaRequest request);

    Estanteria update(Long id, EstanteriaRequest request);

    void deleteById(Long id);

    List<Estanteria> findByCategoria(String categoria);

    List<Estanteria> findByCategoriaContaining(String categoria);

    List<Estanteria> findByPiso(Piso piso);
}
