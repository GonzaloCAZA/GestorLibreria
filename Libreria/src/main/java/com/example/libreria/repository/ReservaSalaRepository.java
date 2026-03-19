package com.example.libreria.repository;

import com.example.libreria.domain.ReservaSala;
import com.example.libreria.domain.Sala;
import com.example.libreria.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ReservaSalaRepository extends JpaRepository<ReservaSala, Long> {

    List<ReservaSala> findByIdUsuario(Usuario idUsuario);

    List<ReservaSala> findByIdSala(Sala idSala);

    List<ReservaSala> findByFechaReserva(Instant fechaReserva);

    List<ReservaSala> findByFechaFinReserva(Instant fechaFinReserva);
}
