package com.example.libreria.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "pisos")
public class Piso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "num_piso", nullable = false)
    private Integer numPiso;

    @Column(name = "nombre", nullable = false, length = 64)
    private String nombre;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "creado")
    private Instant creado;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "actualizado")
    private Instant actualizado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumPiso() {
        return numPiso;
    }

    public void setNumPiso(Integer numPiso) {
        this.numPiso = numPiso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Instant getCreado() {
        return creado;
    }

    public void setCreado(Instant creado) {
        this.creado = creado;
    }

    public Instant getActualizado() {
        return actualizado;
    }

    public void setActualizado(Instant actualizado) {
        this.actualizado = actualizado;
    }

}