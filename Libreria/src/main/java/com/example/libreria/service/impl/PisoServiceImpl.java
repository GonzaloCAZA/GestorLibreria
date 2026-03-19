package com.example.libreria.service.impl;

import com.example.libreria.repository.PisoRepository;
import com.example.libreria.service.PisoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PisoServiceImpl implements PisoService {

    private final PisoRepository pisoRepository;

    public PisoServiceImpl(PisoRepository pisoRepository) {
        this.pisoRepository = pisoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Piso> findAll() {
        return pisoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Piso findById(Long id) {
        return pisoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Piso no encontrado con id: " + id));
    }

    @Override
    public Piso save(Piso piso) {
        return pisoRepository.save(piso);
    }

    @Override
    public Piso update(Long id, Piso piso) {
        Piso pisoExistente = findById(id);
        pisoExistente.setNumPiso(piso.getNumPiso());
        pisoExistente.setNombre(piso.getNombre());
        pisoExistente.setCreado(piso.getCreado());
        pisoExistente.setActualizado(piso.getActualizado());
        return pisoRepository.save(pisoExistente);
    }

    @Override
    public void deleteById(Long id) {
        Piso piso = findById(id);
        pisoRepository.delete(piso);
    }

    @Override
    @Transactional(readOnly = true)
    public Piso findByNumPiso(Integer numPiso) {
        return pisoRepository.findByNumPiso(numPiso)
                .orElseThrow(() -> new EntityNotFoundException("Piso no encontrado con numero: " + numPiso));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Piso> findByNombre(String nombre) {
        return pisoRepository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Piso> findByNombreContaining(String nombre) {
        return pisoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
