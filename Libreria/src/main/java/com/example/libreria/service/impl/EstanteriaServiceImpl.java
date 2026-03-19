package com.example.libreria.service.impl;

import com.example.libreria.repository.EstanteriaRepository;
import com.example.libreria.service.EstanteriaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EstanteriaServiceImpl implements EstanteriaService {

    private final EstanteriaRepository estanteriaRepository;

    public EstanteriaServiceImpl(EstanteriaRepository estanteriaRepository) {
        this.estanteriaRepository = estanteriaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Estanteria> findAll() {
        return estanteriaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Estanteria findById(Long id) {
        return estanteriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estanteria no encontrada con id: " + id));
    }

    @Override
    public Estanteria save(Estanteria estanteria) {
        return estanteriaRepository.save(estanteria);
    }

    @Override
    public Estanteria update(Long id, Estanteria estanteria) {
        Estanteria estanteriaExistente = findById(id);
        estanteriaExistente.setCategoria(estanteria.getCategoria());
        estanteriaExistente.setIdPiso(estanteria.getIdPiso());
        estanteriaExistente.setCreado(estanteria.getCreado());
        estanteriaExistente.setActualizado(estanteria.getActualizado());
        return estanteriaRepository.save(estanteriaExistente);
    }

    @Override
    public void deleteById(Long id) {
        Estanteria estanteria = findById(id);
        estanteriaRepository.delete(estanteria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Estanteria> findByCategoria(String categoria) {
        return estanteriaRepository.findByCategoria(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Estanteria> findByCategoriaContaining(String categoria) {
        return estanteriaRepository.findByCategoriaContainingIgnoreCase(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Estanteria> findByPiso(Piso piso) {
        return estanteriaRepository.findByIdPiso(piso);
    }
}
