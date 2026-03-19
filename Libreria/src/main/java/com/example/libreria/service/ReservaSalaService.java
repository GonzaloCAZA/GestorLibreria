package com.example.libreria.service;

import com.example.libreria.domain.ReservaSala;
import com.example.libreria.domain.Sala;
import com.example.libreria.domain.Usuario;

import java.time.Instant;
import java.util.List;

public interface ReservaSalaService {

    List<ReservaSala> findAll();

    ReservaSala findById(Long id);

    ReservaSala save(ReservaSala reservaSala);

    ReservaSala update(Long id, ReservaSala reservaSala);

    void deleteById(Long id);

    List<ReservaSala> findByUsuario(Usuario usuario);

    List<ReservaSala> findBySala(Sala sala);

    List<ReservaSala> findByFechaReserva(Instant fechaReserva);

    List<ReservaSala> findByFechaFinReserva(Instant fechaFinReserva);
}
