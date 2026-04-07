package com.example.libreria.repository;

import com.example.libreria.domain.CodigoRecuperacion;
import com.example.libreria.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CodigoRecuperacionRepository extends JpaRepository<CodigoRecuperacion, Long> {

    List<CodigoRecuperacion> findByIdUsuarioAndUsadoEnIsNull(Usuario usuario);

    Optional<CodigoRecuperacion> findTopByIdUsuarioAndCodigoAndUsadoEnIsNullOrderByCreadoDesc(Usuario usuario, String codigo);

    void deleteByExpiraEnBefore(Instant instant);
}
