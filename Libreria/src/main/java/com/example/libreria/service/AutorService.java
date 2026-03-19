package com.example.libreria.service;

import java.time.LocalDate;
import java.util.List;

public interface AutorService {

    List<Autor> findAll();

    Autor findById(Long id);

    Autor save(Autor autor);

    Autor update(Long id, Autor autor);

    void deleteById(Long id);

    Autor findByNombre(String nombre);

    List<Autor> findByNacionalidad(String nacionalidad);

    List<Autor> findByFechaNacimiento(LocalDate fechaNacimiento);

    List<Autor> findByNombreContaining(String nombre);
}
