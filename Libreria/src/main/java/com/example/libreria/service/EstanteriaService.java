package com.example.libreria.service;

import java.util.List;

public interface EstanteriaService {

    List<Estanteria> findAll();

    Estanteria findById(Long id);

    Estanteria save(Estanteria estanteria);

    Estanteria update(Long id, Estanteria estanteria);

    void deleteById(Long id);

    List<Estanteria> findByCategoria(String categoria);

    List<Estanteria> findByCategoriaContaining(String categoria);

    List<Estanteria> findByPiso(Piso piso);
}
