package com.example.libreria.repository;

import com.example.libreria.domain.ReservarSala;
import com.example.libreria.domain.Sala;
import com.example.libreria.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ReservaSalaRepository extends JpaRepository<ReservarSala, Long> {

    List<ReservarSala> findByIdUsuario(Usuario idUsuario);

    List<ReservarSala> findByIdSala(Sala idSala);

    List<ReservarSala> findByFechaReserva(Instant fechaReserva);

    List<ReservarSala> findByFechaFinReserva(Instant fechaFinReserva);
}
