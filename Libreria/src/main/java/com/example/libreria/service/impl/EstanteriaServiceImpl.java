package com.example.libreria.service.impl;

import com.example.libreria.domain.Estanteria;
import com.example.libreria.domain.Piso;
import com.example.libreria.dto.catalogo.EstanteriaRequest;
import com.example.libreria.repository.EstanteriaRepository;
import com.example.libreria.repository.PisoRepository;
import com.example.libreria.service.EstanteriaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EstanteriaServiceImpl implements EstanteriaService {

    private final EstanteriaRepository estanteriaRepository;
    private final PisoRepository pisoRepository;

    public EstanteriaServiceImpl(EstanteriaRepository estanteriaRepository, PisoRepository pisoRepository) {
        this.estanteriaRepository = estanteriaRepository;
        this.pisoRepository = pisoRepository;
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
    public Estanteria save(EstanteriaRequest request) {
        Estanteria estanteria = new Estanteria();
        estanteria.setCategoria(request.categoria());
        estanteria.setIdPiso(resolvePiso(request));
        return estanteriaRepository.save(estanteria);
    }

    @Override
    public Estanteria update(Long id, EstanteriaRequest request) {
        Estanteria estanteriaExistente = findById(id);
        estanteriaExistente.setCategoria(request.categoria());
        estanteriaExistente.setIdPiso(resolvePiso(request));
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

    private Piso resolvePiso(EstanteriaRequest request) {
        Long pisoId = request.resolvePisoId();
        if (pisoId == null) {
            throw new IllegalArgumentException("El campo pisoId es obligatorio");
        }
        return pisoRepository.findById(pisoId)
                .orElseThrow(() -> new EntityNotFoundException("Piso no encontrado con id: " + pisoId));
    }
}
