package com.example.libreria.service;

import com.example.libreria.domain.Autor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AutorService {

    List<Autor> findAll();
    Page<Autor> findAll(Pageable pageable);

    Autor findById(Long id);

    Autor save(Autor autor);

    Autor update(Long id, Autor autor);

    void deleteById(Long id);

    Autor findByNombre(String nombre);

    List<Autor> findByNacionalidad(String nacionalidad);
    Page<Autor> findByNacionalidad(String nacionalidad, Pageable pageable);

    List<Autor> findByFechaNacimiento(LocalDate fechaNacimiento);
    Page<Autor> findByFechaNacimiento(LocalDate fechaNacimiento, Pageable pageable);

    List<Autor> findByNombreContaining(String nombre);
    Page<Autor> findByNombreContaining(String nombre, Pageable pageable);
}
