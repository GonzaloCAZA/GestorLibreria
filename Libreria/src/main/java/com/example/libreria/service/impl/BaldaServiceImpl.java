package com.example.libreria.service.impl;

import com.example.libreria.domain.Balda;
import com.example.libreria.domain.Estanteria;
import com.example.libreria.repository.BaldaRepository;
import com.example.libreria.service.BaldaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BaldaServiceImpl implements BaldaService {

    private final BaldaRepository baldaRepository;

    public BaldaServiceImpl(BaldaRepository baldaRepository) {
        this.baldaRepository = baldaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Balda> findAll() {
        return baldaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Balda findById(Long id) {
        return baldaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Balda no encontrada con id: " + id));
    }

    @Override
    public Balda save(Balda balda) {
        return baldaRepository.save(balda);
    }

    @Override
    public Balda update(Long id, Balda balda) {
        Balda baldaExistente = findById(id);
        baldaExistente.setNumero(balda.getNumero());
        baldaExistente.setIdEstanteria(balda.getIdEstanteria());
        baldaExistente.setCreado(balda.getCreado());
        baldaExistente.setActualizado(balda.getActualizado());
        return baldaRepository.save(baldaExistente);
    }

    @Override
    public void deleteById(Long id) {
        Balda balda = findById(id);
        baldaRepository.delete(balda);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Balda> findByNumero(Integer numero) {
        return baldaRepository.findByNumero(numero);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Balda> findByEstanteria(Estanteria estanteria) {
        return baldaRepository.findByIdEstanteria(estanteria);
    }
}
