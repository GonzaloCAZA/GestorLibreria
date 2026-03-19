package com.example.libreria.service;

import com.example.libreria.domain.ReservarSala;
import com.example.libreria.domain.Sala;
import com.example.libreria.domain.Usuario;

import java.time.Instant;
import java.util.List;

public interface ReservaSalaService {

    List<ReservarSala> findAll();

    ReservarSala findById(Long id);

    ReservarSala save(ReservarSala reservarSala);

    ReservarSala update(Long id, ReservarSala reservarSala);

    void deleteById(Long id);

    List<ReservarSala> findByUsuario(Usuario usuario);

    List<ReservarSala> findBySala(Sala sala);

    List<ReservarSala> findByFechaReserva(Instant fechaReserva);

    List<ReservarSala> findByFechaFinReserva(Instant fechaFinReserva);
}
