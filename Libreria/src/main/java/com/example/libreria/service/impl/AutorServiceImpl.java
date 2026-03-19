package com.example.libreria.service.impl;

import com.example.libreria.repository.AutorRepository;
import com.example.libreria.service.AutorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AutorServiceImpl implements AutorService {

    private final AutorRepository autorRepository;

    public AutorServiceImpl(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Autor> findAll() {
        return autorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Autor findById(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Autor no encontrado con id: " + id));
    }

    @Override
    public Autor save(Autor autor) {
        return autorRepository.save(autor);
    }

    @Override
    public Autor update(Long id, Autor autor) {
        Autor autorExistente = findById(id);
        autorExistente.setNombre(autor.getNombre());
        autorExistente.setFechaNacimiento(autor.getFechaNacimiento());
        autorExistente.setNacionalidad(autor.getNacionalidad());
        autorExistente.setCreado(autor.getCreado());
        autorExistente.setActualizado(autor.getActualizado());
        return autorRepository.save(autorExistente);
    }

    @Override
    public void deleteById(Long id) {
        Autor autor = findById(id);
        autorRepository.delete(autor);
    }

    @Override
    @Transactional(readOnly = true)
    public Autor findByNombre(String nombre) {
        return autorRepository.findByNombre(nombre)
                .orElseThrow(() -> new EntityNotFoundException("Autor no encontrado con nombre: " + nombre));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Autor> findByNacionalidad(String nacionalidad) {
        return autorRepository.findByNacionalidad(nacionalidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Autor> findByFechaNacimiento(LocalDate fechaNacimiento) {
        return autorRepository.findByFechaNacimiento(fechaNacimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Autor> findByNombreContaining(String nombre) {
        return autorRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
