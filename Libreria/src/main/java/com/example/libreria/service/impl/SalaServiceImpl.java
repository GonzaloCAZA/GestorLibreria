package com.example.libreria.service.impl;

import com.example.libreria.domain.Piso;
import com.example.libreria.domain.Sala;
import com.example.libreria.repository.SalaRepository;
import com.example.libreria.service.SalaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SalaServiceImpl implements SalaService {

    private final SalaRepository salaRepository;

    public SalaServiceImpl(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sala> findAll() {
        return salaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sala> findAll(Pageable pageable) {
        return salaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Sala findById(Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sala no encontrada con id: " + id));
    }

    @Override
    public Sala save(Sala sala) {
        return salaRepository.save(sala);
    }

    @Override
    public Sala update(Long id, Sala sala) {
        Sala salaExistente = findById(id);
        salaExistente.setNombre(sala.getNombre());
        salaExistente.setMaximoPersonas(sala.getMaximoPersonas());
        salaExistente.setIdPiso(sala.getIdPiso());
        salaExistente.setCreado(sala.getCreado());
        salaExistente.setActualizado(sala.getActualizado());
        return salaRepository.save(salaExistente);
    }

    @Override
    public void deleteById(Long id) {
        Sala sala = findById(id);
        salaRepository.delete(sala);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sala> findByNombre(String nombre) {
        return salaRepository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sala> findByNombreContaining(String nombre) {
        return salaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sala> findByNombreContaining(String nombre, Pageable pageable) {
        return salaRepository.findByNombreContainingIgnoreCase(nombre, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sala> findByMaximoPersonas(Integer maximoPersonas) {
        return salaRepository.findByMaximoPersonas(maximoPersonas);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sala> findByMaximoPersonas(Integer maximoPersonas, Pageable pageable) {
        return salaRepository.findByMaximoPersonas(maximoPersonas, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sala> findByPiso(Piso piso) {
        return salaRepository.findByIdPiso(piso);
    }
}
