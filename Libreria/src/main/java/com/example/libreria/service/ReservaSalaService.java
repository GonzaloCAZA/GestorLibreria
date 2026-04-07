package com.example.libreria.service;

import com.example.libreria.domain.ReservarSala;
import com.example.libreria.domain.Sala;
import com.example.libreria.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface ReservaSalaService {

    List<ReservarSala> findAll();
    Page<ReservarSala> findAll(Pageable pageable);

    ReservarSala findById(Long id);

    ReservarSala save(ReservarSala reservarSala);

    ReservarSala update(Long id, ReservarSala reservarSala);

    void deleteById(Long id);

    List<ReservarSala> findByUsuario(Usuario usuario);

    List<ReservarSala> findBySala(Sala sala);
    List<ReservarSala> findByFechaReservaAfter(Instant fecha);
    Page<ReservarSala> findByFechaReservaAfter(Instant fecha, Pageable pageable);
    List<ReservarSala> findByFechaReserva(Instant fechaReserva);
    Page<ReservarSala> findByFechaReserva(Instant fechaReserva, Pageable pageable);

    List<ReservarSala> findByFechaFinReserva(Instant fechaFinReserva);
    Page<ReservarSala> findByFechaFinReserva(Instant fechaFinReserva, Pageable pageable);
}
