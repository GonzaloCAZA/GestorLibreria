package com.example.libreria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaldaRepository extends JpaRepository<Balda, Long> {

    List<Balda> findByNumero(Integer numero);

    List<Balda> findByIdEstanteria(Estanteria idEstanteria);
}
