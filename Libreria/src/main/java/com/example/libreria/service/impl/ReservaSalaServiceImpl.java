package com.example.libreria.service.impl;

import com.example.libreria.domain.ReservarSala;
import com.example.libreria.domain.Sala;
import com.example.libreria.domain.Usuario;
import com.example.libreria.repository.ReservaSalaRepository;
import com.example.libreria.service.ReservaSalaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class ReservaSalaServiceImpl implements ReservaSalaService {

    private final ReservaSalaRepository reservaSalaRepository;

    public ReservaSalaServiceImpl(ReservaSalaRepository reservaSalaRepository) {
        this.reservaSalaRepository = reservaSalaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservarSala> findAll() {
        return reservaSalaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservarSala findById(Long id) {
        return reservaSalaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con id: " + id));
    }

    @Override
    public ReservarSala save(ReservarSala reservaSala) {
        return reservaSalaRepository.save(reservaSala);
    }

    @Override
    public ReservarSala update(Long id, ReservarSala reservaSala) {
        ReservarSala reservaExistente = findById(id);
        reservaExistente.setIdUsuario(reservaSala.getIdUsuario());
        reservaExistente.setIdSala(reservaSala.getIdSala());
        reservaExistente.setFechaReserva(reservaSala.getFechaReserva());
        reservaExistente.setFechaFinReserva(reservaSala.getFechaFinReserva());
        reservaExistente.setCreado(reservaSala.getCreado());
        reservaExistente.setActualizado(reservaSala.getActualizado());
        return reservaSalaRepository.save(reservaExistente);
    }

    @Override
    public void deleteById(Long id) {
        ReservarSala reservaSala = findById(id);
        reservaSalaRepository.delete(reservaSala);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservarSala> findByUsuario(Usuario usuario) {
        return reservaSalaRepository.findByIdUsuario(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservarSala> findBySala(Sala sala) {
        return reservaSalaRepository.findByIdSala(sala);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservarSala> findByFechaReserva(Instant fechaReserva) {
        return reservaSalaRepository.findByFechaReserva(fechaReserva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservarSala> findByFechaFinReserva(Instant fechaFinReserva) {
        return reservaSalaRepository.findByFechaFinReserva(fechaFinReserva);
    }
}
